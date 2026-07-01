package com.lucasdpm.cognilink.ui.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.lucasdpm.cognilink.data.model.Deck
import com.lucasdpm.cognilink.data.model.FlashcardWithStats
import com.lucasdpm.cognilink.data.model.StudyContext
import com.lucasdpm.cognilink.data.repository.DeckRepository
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.data.repository.StudyContextRepository
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.ui.states.DeckFormUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

sealed class DeckFormEvent {
    data class NavigateToCreateContext(val deckId: String) : DeckFormEvent()
}

class DeckFormViewModel(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    private val studyContextRepository: StudyContextRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val notificationService: AppNotificationService
) : ViewModel() {

    companion object {
        private const val TAG = "DeckFormViewModel"
    }

    private val _uiState = MutableStateFlow(DeckFormUiState())
    val uiState: StateFlow<DeckFormUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DeckFormEvent>()
    val events: SharedFlow<DeckFormEvent> = _events.asSharedFlow()

    private var deckFormJob: Job? = null

    fun initialize(deckId: String?, userId: String) {
        // Se já inicializamos este ViewModel (seja rascunho ou edição), não fazemos nada.
        if (_uiState.value.deckId.isNotEmpty()) return

        val targetId = deckId ?: UUID.randomUUID().toString()

        _uiState.update {
            it.copy(
                userId = userId,
                deckId = targetId,
                isEditMode = deckId != null
            )
        }

        deckFormJob?.cancel()
        deckFormJob = viewModelScope.launch {
            if (deckId != null) {
                _uiState.update { it.copy(isLoading = true) }
                loadDeckData()
            } else {
                // Cria o rascunho imediatamente para permitir adicionar flashcards
                saveDraftDeck(targetId, userId)
                _uiState.update { it.copy(wasEdited = true) }
            }

            // Começa a observar os flashcards deste ID para sempre
            observeFlashcards(targetId)
            observeStudyContexts(targetId, userId)
        }
    }

    private fun observeStudyContexts(deckId: String, userId: String) {
        viewModelScope.launch {
            studyContextRepository.getContextsForDeck(deckId).collectLatest { list ->
                _uiState.update { it.copy(deckContexts = list) }
            }
        }
        viewModelScope.launch {
            studyContextRepository.getContextsForUser(userId).collectLatest { list ->
                _uiState.update { it.copy(allUserContexts = list) }
            }
        }
    }

    fun markCurrentLocation() {
        viewModelScope.launch {
            try {
                // Tenta o cache primeiro
                var location = fusedLocationProviderClient.lastLocation.await()
                
                // Se o cache for nulo, solicita a localização atual em tempo real
                if (location == null) {
                    val request = CurrentLocationRequest.Builder()
                        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                        .build()
                    location = fusedLocationProviderClient.getCurrentLocation(request, null).await()
                }

                if (location != null) {
                    val existingContext = findNearbyContext(location.latitude, location.longitude)
                    
                    if (existingContext != null) {
                        studyContextRepository.linkDeckToContext(_uiState.value.deckId, existingContext.id)
                        notificationService.showSuccess("Local '${existingContext.name}' vinculado ao baralho.")
                    } else {
                        _events.emit(DeckFormEvent.NavigateToCreateContext(_uiState.value.deckId))
                    }
                } else {
                    notificationService.showError("Não foi possível obter a localização. Verifique se o GPS está em alta precisão.")
                }
            } catch (e: SecurityException) {
                notificationService.showError("Permissão de localização negada.")
            } catch (e: Exception) {
                Log.e(TAG, "markCurrentLocation: ", e)
                notificationService.showError("Erro ao obter localização.")
            }
        }
    }

    private fun findNearbyContext(lat: Double, lng: Double): StudyContext? {
        val results = FloatArray(1)
        return _uiState.value.allUserContexts.find { context ->
            Location.distanceBetween(lat, lng, context.latitude, context.longitude, results)
            results[0] <= context.radius // Se estiver dentro do raio do local
        }
    }

    fun toggleContextSelectionDialog() {
        _uiState.update { it.copy(showContextSelectionDialog = !it.showContextSelectionDialog) }
    }

    fun linkContext(contextId: String) {
        viewModelScope.launch {
            studyContextRepository.linkDeckToContext(_uiState.value.deckId, contextId)
        }
    }

    fun unlinkContext(contextId: String) {
        viewModelScope.launch {
            studyContextRepository.unlinkDeckFromContext(_uiState.value.deckId, contextId)
        }
    }

    private suspend fun saveDraftDeck(id: String, userId: String) {
        try {
            val draft = Deck(
                id = id, userId = userId, name = "", description = "",
                categories = emptyList(), difficulty = DifficultyLevel.EASY,
                mastery = 0f, totalCards = 0, cardsToReview = 0,
            )
            deckRepository.saveDeck(draft, userId)
            _uiState.update { it.copy(wasEdited = true) }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e(TAG, "saveDraftDeck: error on save draft deck", e)
            _uiState.update {
                it.copy(
                    errorMessage = "Erro ao preparar rascunho",
                    showCriticalErrorDialog = true
                )
            }
        }
    }

    private fun loadDeckData() {
        val state = _uiState.value
        viewModelScope.launch {
            try {
                deckRepository.getDeckById(state.deckId, state.userId!!).collect { deck ->
                    deck?.let { d ->
                        _uiState.update {
                            it.copy(
                                deckName = it.deckName.ifEmpty { d.name },
                                deckDescription = it.deckDescription.ifEmpty { d.description },
                                deckCategories = it.deckCategories.ifEmpty { d.categories },
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update { it.copy(isLoading = false) }
                Log.e(TAG, "loadDeckData: Erro ao carregar dados do baralho", e)
            }
        }
    }

    private fun observeFlashcards(deckId: String) {
        viewModelScope.launch {
            try {
                flashcardRepository.getFlashcardsForDeck(deckId).collect { list ->
                    _uiState.update { 
                        it.copy(
                            deckFlashcards = list,
                            filteredFlashcards = filterFlashcards(list, it.searchInput)
                        ) 
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "observeFlashcards: Erro ao observar flashcards", e)
            }
        }
    }

    fun onSearchValueChange(newValue: String) {
        _uiState.update { 
            it.copy(
                searchInput = newValue,
                filteredFlashcards = filterFlashcards(it.deckFlashcards, newValue)
            ) 
        }
    }

    private fun filterFlashcards(list: List<FlashcardWithStats>, query: String): List<FlashcardWithStats> {
        return if (query.isBlank()) {
            list
        } else {
            list.filter { it.flashcard.question.contains(query, ignoreCase = true) }
        }
    }

    fun saveDeck() {
        if (!validate()) return
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val deck = Deck(
                    id = state.deckId,
                    userId = state.userId!!,
                    name = state.deckName,
                    description = state.deckDescription,
                    categories = state.deckCategories.filter { it.isNotBlank() },
                    difficulty = DifficultyLevel.EASY,
                    mastery = 0f,
                    totalCards = state.deckFlashcards.size,
                    cardsToReview = state.deckFlashcards.size // Simplificado
                )
                deckRepository.saveDeck(deck, state.userId)
                _uiState.update {
                    it.copy(
                        isSaved = true,
                        isEditMode = true,
                        isSaving = false
                    )
                }
                notificationService.showSuccess("Baralho salvo com sucesso!")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update { it.copy(isSaving = false, isLoading = false) }
                notificationService.showError("Erro ao salvar baralho")
                Log.e(TAG, "saveDeck: Erro ao salvar baralho", e)
            }
        }
    }

    fun discardDeck() {
        val state = _uiState.value
        // Só deleta se for um novo baralho que nunca foi salvo oficialmente
        if (!state.isEditMode && !state.isSaved) {
            viewModelScope.launch {
                try {
                    deckRepository.deleteDeck(state.deckId, state.userId!!)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    Log.e(TAG, "discardDeck: Erro ao excluir rascunho", e)
                }
            }
        }
    }

    // Funções de UI simples
    fun onDeckNameChange(n: String) =
        _uiState.update { it.copy(deckName = n, wasEdited = true, deckNameError = null) }

    fun onDeckDescriptionChange(d: String) =
        _uiState.update { it.copy(deckDescription = d, wasEdited = true) }

    fun toggleRemoveMode() = _uiState.update { it.copy(isRemoveMode = !it.isRemoveMode) }
    fun toggleChangeDialog() = _uiState.update { it.copy(showChangeDialog = !it.showChangeDialog) }

    fun toggleAddFlashcardDialog() =
        _uiState.update { it.copy(showAddFlashcardDialog = !it.showAddFlashcardDialog) }

    fun removeFlashcard(id: String) =
        viewModelScope.launch { 
            try {
                flashcardRepository.deleteFlashcard(id)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                notificationService.showError("Erro ao excluir flashcard")
                Log.e(TAG, "removeFlashcard: Erro ao excluir flashcard", e)
            }
        }

    // Categorias
    fun openCategoryDialog(c: String? = null) = _uiState.update {
        it.copy(
            categoryBeingEdited = c,
            categoryText = c ?: "",
            showCategoryDialog = true
        )
    }

    fun closeCategoryDialog() =
        _uiState.update { it.copy(showCategoryDialog = false, categoryText = "") }

    fun onCategoryTextChange(t: String) = _uiState.update { it.copy(categoryText = t) }
    fun handleCategoryConfirmation() {
        val state = _uiState.value
        if (state.categoryText.isBlank()) return
        val newCats =
            if (state.categoryBeingEdited == null) state.deckCategories + state.categoryText
            else state.deckCategories.map { if (it == state.categoryBeingEdited) state.categoryText else it }
        _uiState.update {
            it.copy(
                deckCategories = newCats.distinct(),
                showCategoryDialog = false,
                wasEdited = true
            )
        }
    }

    fun removeCategory(c: String) =
        _uiState.update { it.copy(deckCategories = it.deckCategories - c, wasEdited = true) }

    private fun validate(): Boolean {
        if (_uiState.value.deckName.isBlank()) {
            _uiState.update { it.copy(deckNameError = "O nome é obrigatório") }
            return false
        }
        return true
    }
}
