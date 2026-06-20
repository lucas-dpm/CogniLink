package com.lucasdpm.cognilink.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.repository.DeckRepository
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.ui.states.DeckUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

class DeckViewModel(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    private val notificationService: AppNotificationService
) : ViewModel() {

    companion object {
        private const val TAG = "DeckViewModel"
    }

    private val _uiState = MutableStateFlow(DeckUiState())
    val uiState: StateFlow<DeckUiState> = _uiState.asStateFlow()

    fun initialize(deckId: String, userId: String) {
        if (_uiState.value.deckId == deckId && _uiState.value.userId == userId) return
        _uiState.update { it.copy(deckId = deckId, userId = userId) }
        loadDeckDetails(deckId, userId)
    }

    private fun loadDeckDetails(deckId: String, userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                deckRepository.getDeckById(deckId, userId).collect { deck ->
                    if (deck != null) {
                        _uiState.update { it.copy(currentDeck = deck, isLoading = false) }
                        // Flashcards já estão sendo observados reativamente via loadFlashcards
                    } else if (!_uiState.value.isDeleting) {
                        _uiState.update { 
                            it.copy(
                                errorMessage = "Baralho não encontrado", 
                                isLoading = false,
                                showCriticalErrorDialog = true
                            ) 
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Erro ao carregar baralho",
                        showCriticalErrorDialog = true
                    )
                }
                Log.e(TAG, "loadDeckDetails: Erro ao carregar baralho", e)
            }
        }

        // Inicia a observação dos flashcards separadamente
        loadFlashcards(deckId)
    }

    private fun loadFlashcards(deckId: String) {
        viewModelScope.launch {
            try {
                flashcardRepository.getFlashcardsForDeck(deckId).collect { flashcards ->
                    _uiState.update { it.copy(flashcards = flashcards.take(3)) }
                }
            } catch (e: Exception) {
                notificationService.showError("Erro ao carregar flashcards")
                Log.e(TAG, "loadFlashcards: Erro ao carregar flashcards", e)
            }
        }
    }

    fun loadAllFlashcards() {
        val deckId = _uiState.value.deckId ?: return
        viewModelScope.launch {
            try {
                flashcardRepository.getFlashcardsForDeck(deckId).collect { flashcards ->
                    _uiState.update { it.copy(flashcards = flashcards) }
                }
            } catch (e: Exception) {
                notificationService.showError("Erro ao carregar flashcards")
                Log.e(TAG, "loadAllFlashcards: Erro ao carregar todos os flashcards", e)
            }
        }
    }

    fun formatNextReview(nextReviewTimestamp: Long?): String {
        if (nextReviewTimestamp == null) return "Novo card"

        val now = Clock.System.now()
        val targetInstant = Instant.fromEpochMilliseconds(nextReviewTimestamp)

        // Se o momento da revisão já passou
        if (targetInstant <= now) return "Revisar agora"

        val tz = TimeZone.currentSystemDefault()
        val today = now.toLocalDateTime(tz).date
        val targetDate = targetInstant.toLocalDateTime(tz).date

        // Calcula a diferença em dias de calendário (ex: de 23:59 de hoje para 00:01 de amanhã = 1 dia)
        val days = today.daysUntil(targetDate)

        return when {
            days <= 0 -> "Revisar hoje"
            days == 1 -> "Revisar amanhã"
            days < 30 -> "Revisar em $days dias"
            else -> {
                val day = targetDate.day.toString().padStart(2, '0')
                val month = targetDate.month.number.toString().padStart(2, '0')
                "Revisar em $day/$month/${targetDate.year}"
            }
        }
    }

    fun deleteDeck() {
        val deckId = _uiState.value.deckId ?: return
        val userId = _uiState.value.userId ?: return
        _uiState.update { it.copy(isDeleting = true) }
        viewModelScope.launch {
            try {
                deckRepository.deleteDeck(deckId, userId)
                notificationService.showSuccess("Baralho excluído com sucesso!")
            } catch (e: Exception) {
                _uiState.update { it.copy(isDeleting = false) }
                notificationService.showError("Erro ao excluir baralho")
                Log.e(TAG, "deleteDeck: ", e)
            }
        }
    }

    fun toggleMenu() {
        _uiState.update { it.copy(isMenuExpanded = !it.isMenuExpanded) }
    }

    fun toggleAddFlashcardDialog() {
        _uiState.update { it.copy(isAddFlashcardDialogOpen = !it.isAddFlashcardDialogOpen) }
    }

    fun toggleDeleteDeckDialog() {
        _uiState.update { it.copy(isDeleteDeckDialogOpen = !it.isDeleteDeckDialogOpen) }
    }
}
