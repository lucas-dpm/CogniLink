package com.lucasdpm.cognilink.ui.viewmodels

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

class DeckViewModel(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository,
    private val notificationService: AppNotificationService
) : ViewModel() {

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
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Erro ao carregar baralho",
                        showCriticalErrorDialog = true
                    )
                }
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
            } catch (_: Exception) {
                notificationService.showError("Erro ao carregar flashcards")
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
            } catch (_: Exception) {
                notificationService.showError("Erro ao carregar flashcards")
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
