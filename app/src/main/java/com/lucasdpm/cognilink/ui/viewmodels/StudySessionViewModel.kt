package com.lucasdpm.cognilink.ui.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.data.model.FlashcardStats
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
import com.lucasdpm.cognilink.domain.usecase.CalculateSM2UseCase
import com.lucasdpm.cognilink.domain.usecase.UpdateUserStatsUseCase
import com.lucasdpm.cognilink.domain.usecase.ValidateBasicAnswerUseCase

class StudySessionViewModel(
    private val repository: FlashcardRepository,
    private val validateBasicAnswerUseCase: ValidateBasicAnswerUseCase,
    private val calculateSM2UseCase: CalculateSM2UseCase,
    private val updateUserStatsUseCase: UpdateUserStatsUseCase,
    private val notificationService: AppNotificationService,
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

    fun initializeSession(studyMode: String, contextId: String, userId: String? = null) {
        if (_uiState.value.studyMode == studyMode && _uiState.value.contextId == contextId) return
        
        _uiState.update { it.copy(studyMode = studyMode, contextId = contextId, userId = userId, isLoading = true) }
        
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
                    selectedAnswers = emptyMap(),
                    cardStartTimeSeconds = it.secondsElapsed
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
                    isQuestionVerified = false,
                    cardStartTimeSeconds = it.secondsElapsed
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

            val latencyMs = (currentState.secondsElapsed - currentState.cardStartTimeSeconds) * 1000L
            updateFlashcardStats(currentFlashcard.id, isCorrect, latencyMs)

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

    private fun updateFlashcardStats(flashcardId: String, isCorrect: Boolean, latencyMs: Long) {
        viewModelScope.launch {
            val currentFlashcardWithStats = repository.getFlashcardById(flashcardId)
            val currentStats = currentFlashcardWithStats?.stats ?: FlashcardStats(flashcardId = flashcardId)

            // Cálculo da qualidade baseado no acerto e latência
            val quality = when {
                !isCorrect -> 0
                latencyMs < 3000L -> 5
                latencyMs < 8000L -> 4
                else -> 3
            }

            val sm2Result = calculateSM2UseCase(
                quality = quality,
                previousInterval = currentStats.memoryStabilityDays,
                previousEaseFactor = currentStats.easeFactor,
                previousRepetitions = currentStats.repetitions
            )

            val updatedHits = if (isCorrect) currentStats.hits + 1 else currentStats.hits
            val updatedMisses = if (!isCorrect) currentStats.misses + 1 else currentStats.misses
            val updatedStudyTime = currentStats.studyTime + latencyMs

            val newStats = currentStats.copy(
                hits = updatedHits,
                misses = updatedMisses,
                studyTime = updatedStudyTime,
                nextReview = sm2Result.nextReview,
                averageLatencyMs = if (updatedHits + updatedMisses == 1) latencyMs
                else (currentStats.averageLatencyMs * (currentStats.hits + currentStats.misses) + latencyMs) / (updatedHits + updatedMisses),
                memoryStabilityDays = sm2Result.intervalDays,
                easeFactor = sm2Result.easeFactor,
                repetitions = sm2Result.repetitions,
                consecutiveMisses = if (isCorrect) 0 else currentStats.consecutiveMisses + 1,
                retentionRate = (updatedHits.toFloat() / (updatedHits + updatedMisses).toFloat()),
                mastery = if (sm2Result.repetitions > 0) {
                    // Simples cálculo de domínio baseado em repetições e EF
                    (sm2Result.repetitions * sm2Result.easeFactor / 20f).coerceAtMost(1.0f)
                } else currentStats.mastery
            )

            repository.updateFlashcardStatistics(newStats)

            // Após atualizar os stats do flashcard, atualizamos os stats globais do usuário
            _uiState.value.userId?.let { userId ->
                updateUserStatsUseCase(userId)
            }
        }
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
