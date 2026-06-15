package com.lucasdpm.cognilink.ui.states

import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.data.model.Flashcard
import com.lucasdpm.cognilink.domain.model.ValidationType

data class StudySessionUiState(
    val studyMode: String? = null,
    val contextId: String? = null,
    val sessionFlashcards: List<Flashcard> = emptyList(),
    val currentFlashcardIndex: Int = 0,
    val selectedAnswers: Map<Answer, String> = emptyMap(),
    val isQuestionVerified: Boolean = false,
    val isCloseDialogOpen: Boolean = false,
    val isSessionInsightDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
    val sessionTitle: String = "",
    val secondsElapsed: Long = 0L,
    val sequenceHits: Int = 0,
    val isValidating: Boolean = false,
    val validationType: ValidationType = ValidationType.NONE,
    val isAnswerCorrect: Boolean = false,
    val showCriticalErrorDialog: Boolean = false,
    val errorMessage: String? = null
) {
    val isQuestionAnswered: Boolean = selectedAnswers.isNotEmpty()
    val currentFlashcard: Flashcard? = sessionFlashcards.getOrNull(currentFlashcardIndex)
    val isLastFlashcard: Boolean = currentFlashcardIndex == sessionFlashcards.size - 1 && sessionFlashcards.isNotEmpty()
    val isSessionFinished: Boolean = currentFlashcardIndex >= sessionFlashcards.size && sessionFlashcards.isNotEmpty()
}
