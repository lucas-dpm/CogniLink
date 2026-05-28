package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cognilink.data.model.Answer
import com.example.cognilink.domain.model.FlashcardType
import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.repository.FlashcardRepository
import com.example.cognilink.data.repository.FlashcardRepositoryImpl
import com.example.cognilink.ui.states.FlashcardPlayerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FlashcardPlayerViewModel(
    private val repository: FlashcardRepository = FlashcardRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardPlayerUiState())
    val uiState: StateFlow<FlashcardPlayerUiState> = _uiState.asStateFlow()

    fun loadFlashcards(deckId: Long) {
        viewModelScope.launch {
            val flashcards = repository.getFlashcardsForDeck(deckId)
            if (flashcards.isNotEmpty()) {
                _uiState.update { it.copy(currentFlashcard = flashcards.first()) }
            }
        }
    }

    fun onSelectAnswer(answer: Answer, choice: String = "") {
        _uiState.update { currentState ->
            if (currentState.isQuestionVerified) return@update currentState

            val newAnswers = when (currentState.currentFlashcard?.cardType) {
                FlashcardType.MULTIPLE_CHOICE, FlashcardType.BASIC -> mapOf(answer to choice)
                else -> currentState.selectedAnswers + (answer to choice)
            }
            currentState.copy(selectedAnswers = newAnswers)
        }
    }

    fun nextFlashcard(nextFlashcard: Flashcard) {
        _uiState.update {
            it.copy(
                currentFlashcard = nextFlashcard,
                selectedAnswers = emptyMap(),
                isQuestionVerified = false
            )
        }
    }

    fun verifyQuestion() {
        _uiState.update { it.copy(isQuestionVerified = !it.isQuestionVerified) }
    }

    fun saveFlashcard() {
        _uiState.value.currentFlashcard?.let { flashcard ->
            viewModelScope.launch {
                repository.saveFlashcard(flashcard)
            }
        }
    }
}
