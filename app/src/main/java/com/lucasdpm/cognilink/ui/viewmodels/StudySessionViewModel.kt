package com.lucasdpm.cognilink.ui.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.ui.states.StudySessionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.lucasdpm.cognilink.domain.model.ValidationResult
import com.lucasdpm.cognilink.domain.model.ValidationType
import com.lucasdpm.cognilink.domain.usecase.ValidateBasicAnswerUseCase

class StudySessionViewModel(
    private val repository: FlashcardRepository,
    private val validateBasicAnswerUseCase: ValidateBasicAnswerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudySessionUiState())
    val uiState: StateFlow<StudySessionUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _uiState.update { it.copy(secondsElapsed = it.secondsElapsed + 1) }
            }
        }
    }

    fun initializeSession(studyMode: String, contextId: String) {
        if (_uiState.value.studyMode == studyMode && _uiState.value.contextId == contextId) return
        
        _uiState.update { it.copy(studyMode = studyMode, contextId = contextId, isLoading = true) }
        
        viewModelScope.launch {
            val flashcards = when (studyMode) {
                "DECK" -> repository.getFlashcardsForDeck(contextId).first().map { it.flashcard }
                "LEECHES" -> repository.getLeeches(contextId)?.map { it.flashcard } ?: emptyList()
                "REVIEW" -> repository.getReviewPending(contextId)?.map { it.flashcard } ?: emptyList()
                "FLASHCARD" -> repository.getFlashcardById(contextId)?.flashcard?.let { listOf(it) }
                    ?: emptyList()

                else -> emptyList()
            }
            val sessionTitle = when (studyMode) {
                "DECK" -> repository.getDeckName(contextId) ?: "ESTUDAR DECK"
                "LEECHES" -> "SESSÃO DE LEECHES"
                "REVIEW" -> "REVISÃO"
                "FLASHCARD" -> flashcards.firstOrNull()?.deckId?.let { repository.getDeckName(it) } ?: "ESTUDAR FLASHCARD"
                else -> ""
            }

            if (flashcards.isEmpty()) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Não há flashcards disponíveis para esta sessão.",
                        showCriticalErrorDialog = true
                    ) 
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    sessionFlashcards = flashcards,
                    sessionTitle = sessionTitle,
                    currentFlashcardIndex = 0,
                    isLoading = false,
                    isQuestionVerified = false,
                    selectedAnswers = emptyMap()
                )
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

    fun nextFlashcard() {
        if (_uiState.value.isLastFlashcard) {
            _uiState.update {
                it.copy(
                    isSessionInsightDialogOpen = true
                )
            }
        } else {
            _uiState.update {
                val nextIndex = it.currentFlashcardIndex + 1
                it.copy(
                    currentFlashcardIndex = nextIndex,
                    selectedAnswers = emptyMap(),
                    isQuestionVerified = false
                )
            }
        }
    }

    fun verifyQuestion() {
        val currentState = _uiState.value
        val currentFlashcard = currentState.currentFlashcard ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isValidating = true) }

            var isCorrect: Boolean
            var validationType = ValidationType.NONE
            var aiFeedback: String? = null

            when (currentFlashcard.cardType) {
                FlashcardType.BASIC, FlashcardType.OMISSION -> {
                    val userAnswer = currentState.selectedAnswers.values.firstOrNull() ?: ""
                    val correctAnswer = currentFlashcard.answerOptions.firstOrNull()?.answer ?: ""

                    val result = validateBasicAnswerUseCase(
                        question = currentFlashcard.question,
                        userAnswer = userAnswer,
                        correctAnswer = correctAnswer
                    )

                    when (result) {
                        is ValidationResult.Correct -> {
                            isCorrect = true
                            validationType = ValidationType.CORRECT
                            aiFeedback = result.feedback
                        }
                        is ValidationResult.Fallback -> {
                            isCorrect = false
                            validationType = ValidationType.FALLBACK
                            aiFeedback = result.feedback
                        }
                    }
                }

                FlashcardType.MULTIPLE_CHOICE -> {
                    val selectedAnswer = currentState.selectedAnswers.keys.firstOrNull()
                    val correctAnswer = currentFlashcard.answerOptions.find { it.isCorrect }

                    isCorrect = selectedAnswer?.isCorrect ?: false
                    validationType = if (isCorrect) ValidationType.CORRECT else ValidationType.FALLBACK

                    if (selectedAnswer != null && correctAnswer != null) {
                        val result = validateBasicAnswerUseCase(
                            question = currentFlashcard.question,
                            userAnswer = selectedAnswer.answer,
                            correctAnswer = correctAnswer.answer
                        )
                        aiFeedback = when (result) {
                            is ValidationResult.Correct -> result.feedback
                            is ValidationResult.Fallback -> result.feedback
                        }
                    }
                }

                FlashcardType.TRUE_OR_FALSE -> {
                    isCorrect = currentState.selectedAnswers.all { (answer, choice) ->
                        (choice == "T" && answer.isCorrect) || (choice == "F" && !answer.isCorrect)
                    }
                    validationType = if (isCorrect) ValidationType.CORRECT else ValidationType.FALLBACK
                    
                    val firstAnswer = currentState.selectedAnswers.keys.firstOrNull()
                    val userChoice = currentState.selectedAnswers.values.firstOrNull() ?: ""
                    if (firstAnswer != null) {
                         val result = validateBasicAnswerUseCase(
                            question = currentFlashcard.question,
                            userAnswer = "O usuário marcou a afirmação como ${if(userChoice == "T") "Verdadeira" else "Falsa"}",
                            correctAnswer = "A afirmação é ${if(firstAnswer.isCorrect) "Verdadeira" else "Falsa"}"
                        )
                        aiFeedback = when (result) {
                            is ValidationResult.Correct -> result.feedback
                            is ValidationResult.Fallback -> result.feedback
                        }
                    }
                }

                else -> {
                    isCorrect = true
                }
            }

            _uiState.update {
                it.copy(
                    isQuestionVerified = true,
                    sequenceHits = if (isCorrect) it.sequenceHits + 1 else 0,
                    isAnswerCorrect = isCorrect,
                    validationType = validationType,
                    aiFeedback = aiFeedback,
                    isValidating = false
                )
            }
        }
    }

    fun toggleSessionInsightDialog() {
        _uiState.update { it.copy(isSessionInsightDialogOpen = !it.isSessionInsightDialogOpen) }
    }


    fun toggleCloseDialog() {
        _uiState.update { it.copy(isCloseDialogOpen = !it.isCloseDialogOpen) }
    }

    @SuppressLint("DefaultLocale")
    fun formatSeconds(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        if (h == 0L) return String.format("%02d:%02d", m, s)
        return String.format("%02d:%02d:%02d", h, m, s)
    }
}
