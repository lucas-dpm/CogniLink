package com.lucasdpm.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.Deck
import com.lucasdpm.cognilink.data.repository.DeckRepository
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.ui.states.DeckFormUiState
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckFormViewModel(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DeckFormUiState())
    val uiState: StateFlow<DeckFormUiState> = _uiState.asStateFlow()

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

        if (deckId != null) {
            loadDeckData()
        } else {
            // Cria o rascunho imediatamente para permitir adicionar flashcards
            saveDraftDeck(targetId, userId)
            _uiState.update {it.copy(wasEdited = true)}
        }
        
        // Começa a observar os flashcards deste ID para sempre
        observeFlashcards(targetId)
    }

    private fun saveDraftDeck(id: String, userId: String) {
        viewModelScope.launch {
            try {
                val draft = Deck(
                    id = id, userId = userId, name = "", description = "",
                    categories = emptyList(), difficulty = DifficultyLevel.EASY,
                    mastery = 0f, totalCards = 0, cardsToReview = 0
                )
                deckRepository.saveDeck(draft, userId)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Erro ao preparar rascunho") }
            }
        }
    }

    private fun loadDeckData() {
        val state = _uiState.value
        viewModelScope.launch {
            deckRepository.getDeckById(state.deckId, state.userId!!).collect { deck ->
                deck?.let { d ->
                    _uiState.update { 
                        it.copy(
                            deckName = it.deckName.ifEmpty { d.name },
                            deckDescription = it.deckDescription.ifEmpty { d.description },
                            deckCategories = it.deckCategories.ifEmpty { d.categories }
                        )
                    }
                }
            }
        }
    }

    private fun observeFlashcards(deckId: String) {
        viewModelScope.launch {
            flashcardRepository.getFlashcardsForDeck(deckId).collect { list ->
                _uiState.update { it.copy(deckFlashcards = list) }
            }
        }
    }

    fun saveDeck() {
        if (!validate()) return
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
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
                _uiState.update { it.copy(isSaved = true, isLoading = false, isEditMode = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao salvar") }
            }
        }
    }

    fun discardDeck() {
        val state = _uiState.value
        // Só deleta se for um novo baralho que nunca foi salvo oficialmente
        if (!state.isEditMode && !state.isSaved) {
            viewModelScope.launch {
                deckRepository.deleteDeck(state.deckId, state.userId!!)
            }
        }
    }

    // Funções de UI simples
    fun onDeckNameChange(n: String) = _uiState.update { it.copy(deckName = n, wasEdited = true, deckNameError = null) }
    fun onDeckDescriptionChange(d: String) = _uiState.update { it.copy(deckDescription = d, wasEdited = true) }
    fun toggleRemoveMode() = _uiState.update { it.copy(isRemoveMode = !it.isRemoveMode) }
    fun toggleChangeDialog() = _uiState.update { it.copy(showChangeDialog = !it.showChangeDialog) }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
    fun toggleAddFlashcardDialog() = _uiState.update { it.copy(showAddFlashcardDialog = !it.showAddFlashcardDialog) }
    fun removeFlashcard(id: String) = viewModelScope.launch { flashcardRepository.deleteFlashcard(id) }
    
    // Categorias
    fun openCategoryDialog(c: String? = null) = _uiState.update { it.copy(categoryBeingEdited = c, categoryText = c ?: "", showCategoryDialog = true) }
    fun closeCategoryDialog() = _uiState.update { it.copy(showCategoryDialog = false, categoryText = "") }
    fun onCategoryTextChange(t: String) = _uiState.update { it.copy(categoryText = t) }
    fun handleCategoryConfirmation() {
        val state = _uiState.value
        if (state.categoryText.isBlank()) return
        val newCats = if (state.categoryBeingEdited == null) state.deckCategories + state.categoryText
                      else state.deckCategories.map { if (it == state.categoryBeingEdited) state.categoryText else it }
        _uiState.update { it.copy(deckCategories = newCats.distinct(), showCategoryDialog = false, wasEdited = true) }
    }
    fun removeCategory(c: String) = _uiState.update { it.copy(deckCategories = it.deckCategories - c, wasEdited = true) }

    private fun validate(): Boolean {
        if (_uiState.value.deckName.isBlank()) {
            _uiState.update { it.copy(deckNameError = "O nome é obrigatório") }
            return false
        }
        return true
    }
}
