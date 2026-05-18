package com.example.cognilink.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cognilink.domain.Answer
import com.example.cognilink.domain.DifficultyLevel
import com.example.cognilink.domain.Flashcard
import com.example.cognilink.domain.FlashcardType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FlashcardEditorUiState(
    val questionText: String = "",
    val cardType: FlashcardType = FlashcardType.BASIC,
    val difficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    val answerOptions: List<Answer> = emptyList(),
    val hints: List<String> = emptyList(),
    val isDeleteMode: Boolean = false,
    val currentFlashcardId: Long = 0L,
    val isInitialized: Boolean = false
)

class FlashcardEditorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardEditorUiState())
    val uiState: StateFlow<FlashcardEditorUiState> = _uiState.asStateFlow()

    fun loadFlashcard(flashcard: Flashcard) {
        _uiState.update { currentState ->
            if (currentState.isInitialized) return@update currentState

            currentState.copy(
                currentFlashcardId = flashcard.id,
                questionText = flashcard.question,
                cardType = flashcard.cardType,
                difficulty = flashcard.difficulty,
                answerOptions = flashcard.answerOptions,
                hints = flashcard.hints,
                isInitialized = true
            )
        }
    }

    fun onBasicAnswerChange(newAnswerText: String) {
        _uiState.update { it.copy(answerOptions = listOf(Answer(answer = newAnswerText, isCorrect = true))) }
    }

    fun removeAnswer(answer: Answer) {
        _uiState.update { it.copy(answerOptions = it.answerOptions.filter { a -> a != answer }) }
    }

    fun selectCorrectAnswer(selectedIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                answerOptions = currentState.answerOptions.mapIndexed { index, answer ->
                    answer.copy(isCorrect = index == selectedIndex)
                }
            )
        }
    }

    fun toggleTrueFalseAnswer(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                answerOptions = currentState.answerOptions.mapIndexed { i, a ->
                    if (i == index) a.copy(isCorrect = !a.isCorrect)
                    else a
                }
            )
        }
    }

    fun toggleDeletionMode() {
        _uiState.update { it.copy(isDeleteMode = !it.isDeleteMode) }
    }

    fun onQuestionTextChange(newQuestion: String) {
        _uiState.update { it.copy(questionText = newQuestion) }
    }

    fun onDifficultyChange(newDifficulty: DifficultyLevel) {
        _uiState.update { it.copy(difficulty = newDifficulty) }
    }

    fun updateAnswers(newAnswers: List<Answer>) {
        _uiState.update { it.copy(answerOptions = newAnswers) }
    }

    fun updateHints(newHints: List<String>) {
        _uiState.update { it.copy(hints = newHints) }
    }

    fun onTypeChange(newType: FlashcardType) {
        _uiState.update { it.copy(cardType = newType, answerOptions = emptyList()) }
    }

    fun saveFlashcard() {
        val currentState = _uiState.value
        val flashcardParaSalvar = Flashcard(
            id = currentState.currentFlashcardId,
            question = currentState.questionText,
            cardType = currentState.cardType,
            difficulty = currentState.difficulty,
            answerOptions = currentState.answerOptions,
            hints = currentState.hints
        )
        // repository.save(flashcardParaSalvar)
    }
}
