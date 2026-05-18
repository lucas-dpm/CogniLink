package com.example.cognilink.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cognilink.domain.Answer
import com.example.cognilink.domain.FlashcardType
import com.example.cognilink.domain.Flashcard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FlashcardPlayerUiState(
    val currentFlashcard: Flashcard? = null,
    val selectedAnswers: Map<Answer, String> = emptyMap(),
    val isQuestionVerified: Boolean = false
) {
    val isQuestionAnswered: Boolean = selectedAnswers.isNotEmpty()
}

class FlashcardPlayerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardPlayerUiState())
    val uiState: StateFlow<FlashcardPlayerUiState> = _uiState.asStateFlow()

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
        // Implement save logic using a repository
    }
}
