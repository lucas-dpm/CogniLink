package com.lucasdpm.cognilink.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.data.model.Flashcard
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.ui.states.FlashcardFormUiState
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlashcardFormViewModel(
    private val repository: FlashcardRepository,
    private val notificationService: AppNotificationService
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardFormUiState())
    val uiState: StateFlow<FlashcardFormUiState> = _uiState.asStateFlow()

    fun initialize(deckId: String, flashcardId: String? = null) {
        if (_uiState.value.deckId == deckId && _uiState.value.flashcardId == flashcardId && _uiState.value.isInitialized) return
        
        _uiState.update { currentState ->
            if (flashcardId == null) {
                // Ao criar um novo, garantimos um novo ID e estado limpo
                FlashcardFormUiState(
                    deckId = deckId,
                    flashcardId = UUID.randomUUID().toString(),
                    isEditMode = false,
                    isInitialized = true
                )
            } else {
                currentState.copy(
                    deckId = deckId,
                    flashcardId = flashcardId,
                    isEditMode = true,
                    isInitialized = false
                )
            }
        }
        
        if (flashcardId != null) {
            _uiState.update { it.copy(isLoading = true) }
            loadFlashcard()
        }
    }

    private fun loadFlashcard() {
        val currentState = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val flashcard = repository.getFlashcardById(currentState.flashcardId)?.flashcard
                if (flashcard != null) {
                    _uiState.update { currentState ->
                        if (currentState.isInitialized) return@update currentState

                        currentState.copy(
                            flashcardId = flashcard.id,
                            questionText = flashcard.question,
                            cardType = flashcard.cardType,
                            difficulty = flashcard.difficulty,
                            answerOptions = flashcard.answerOptions,
                            hints = flashcard.hints,
                            isInitialized = true,
                            isLoading = false
                        )

                    }
                }
            } catch (e: Exception) {
                Log.e("FlashcardFormViewModel", "Error loading flashcard: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Sentimos muito, mas não foi possível carregar os dados do flashcard. Tente novamente mais tarde!",
                        showCriticalErrorDialog = true
                    )
                }
            }
        }
    }

    fun onBasicAnswerChange(newAnswerText: String) {
        _uiState.update {
            it.copy(
                answerOptions = listOf(
                    Answer(
                        answer = newAnswerText,
                        isCorrect = true
                    )
                ),
                wasEdited = true,
                answersError = null
            )
        }
    }

    fun removeAnswer(answer: Answer) {
        _uiState.update { it.copy(answerOptions = it.answerOptions.filter { a -> a != answer },wasEdited = true) }
    }

    fun selectCorrectAnswer(selectedIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                answerOptions = currentState.answerOptions.mapIndexed { index, answer ->
                    answer.copy(isCorrect = index == selectedIndex)
                },
                wasEdited = true
            )
        }
    }

    fun toggleTrueFalseAnswer(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                answerOptions = currentState.answerOptions.mapIndexed { i, a ->
                    if (i == index) a.copy(isCorrect = !a.isCorrect)
                    else a
                },
                wasEdited = true
            )
        }
    }

    fun toggleDeletionMode() {
        _uiState.update { it.copy(isDeleteMode = !it.isDeleteMode) }
    }

    fun toggleDeleteDialog(){
        _uiState.update { it.copy(showDeleteDialog = !it.showDeleteDialog) }
    }

    fun toggleChangeDialog(){
        _uiState.update { it.copy(showChangeDialog = !it.showChangeDialog) }
    }

    fun onQuestionTextChange(newQuestion: String) {
        _uiState.update { it.copy(questionText = newQuestion, wasEdited = true, questionTextError = null) }
    }

    fun onDifficultyChange(newDifficulty: DifficultyLevel) {
        _uiState.update { it.copy(difficulty = newDifficulty, wasEdited = true) }
    }

    fun updateAnswers(newAnswers: List<Answer>) {
        _uiState.update { it.copy(answerOptions = newAnswers, wasEdited = true, answersError = null) }
    }

    fun updateHints(newHints: List<String>) {
        _uiState.update { it.copy(hints = newHints, wasEdited = true) }
    }

    fun onTypeChange(newType: FlashcardType) {
        _uiState.update { it.copy(cardType = newType, answerOptions = emptyList(), wasEdited = true) }
    }

    fun toggleMenu(){
        _uiState.update { it.copy(isMenuExpanded = !it.isMenuExpanded) }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        var isValid = true

        if (state.questionText.isBlank()) {
            _uiState.update { it.copy(questionTextError = "O enunciado é obrigatório.") }
            isValid = false
        }

        if (state.answerOptions.isEmpty() || state.answerOptions.all { it.answer.isBlank() }) {
            viewModelScope.launch {
                notificationService.showWarning("Pelo menos uma resposta deve ser fornecida.")
            }
            isValid = false
        } else if (state.cardType != FlashcardType.BASIC && state.answerOptions.none { it.isCorrect }) {
            viewModelScope.launch {
                notificationService.showWarning("Pelo menos uma resposta deve estar marcada como correta.")
            }
            isValid = false
        }

        return isValid
    }

    fun saveFlashcard(): Boolean {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            if (saveFlashcardSuspending()) {
                _uiState.update { it.copy(isSaved = true,isSaving = false) }
            } else {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
        return uiState.value.isSaved
    }

    suspend fun saveFlashcardSuspending(): Boolean {
        if (!validate()) return false

        val currentState = _uiState.value
        val deckId = currentState.deckId ?: return false

        val flashcardToSave = Flashcard(
            id = currentState.flashcardId,
            question = currentState.questionText,
            cardType = currentState.cardType,
            difficulty = currentState.difficulty,
            answerOptions = currentState.answerOptions,
            hints = currentState.hints.filter { it.isNotBlank() },
            deckId = deckId
        )

        return try {
            repository.saveFlashcard(flashcardToSave)
            val isFirstSave = !currentState.isEditMode && !currentState.isNewlyCreated
            _uiState.update { 
                it.copy(
                    isLoading = false, 
                    wasEdited = false,
                    isNewlyCreated = it.isNewlyCreated || isFirstSave
                ) 
            }
            notificationService.showSuccess("Flashcard salvo com sucesso!")
            true
        } catch (e: Exception) {
            Log.e("FlashcardFormViewModel", "saveFlashcardSuspending: Error saving flashcard", e)
            _uiState.update { it.copy(isLoading = false) }
            notificationService.showError("Não foi possível salvar o flashcard. Tente novamente mais tarde!")
            false
        }
    }

    fun deleteFlashcard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.deleteFlashcard(_uiState.value.flashcardId)
                _uiState.update {
                    it.copy(
                        showDeleteDialog = false,
                        isSaved = true,
                        isLoading = false,
                        wasEdited = false
                    )
                }
                notificationService.showSuccess("Flashcard excluído com sucesso!")
            } catch (e: Exception) {
                Log.e("FlashcardFormViewModel", "deleteFlashcard: Error deleting flashcard", e)
                _uiState.update { it.copy(isLoading = false) }
                notificationService.showError("Não foi possível excluir o flashcard. Tente novamente mais tarde!")
            }
        }
    }

    fun discardFlashcard() {
        val currentState = _uiState.value
        if (currentState.isNewlyCreated && !currentState.isSaved) {
            viewModelScope.launch {
                try {
                    repository.deleteFlashcard(currentState.flashcardId)
                } catch (e: Exception) {
                    Log.e("FlashcardFormViewModel", "discardFlashcard: Error deleting flashcard", e)
                }
            }
        }
    }
}
