package com.lucasdpm.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.data.model.Flashcard
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.ui.states.FlashcardFormUiState
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlashcardFormViewModel(
    private val repository: FlashcardRepository
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
            loadFlashcard()
        }
    }

    private fun loadFlashcard() {
        val currentState = _uiState.value
        viewModelScope.launch {
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
                            isInitialized = true
                        )

                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
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
            _uiState.update { it.copy(questionTextError = "O enunciado é obrigatório") }
            isValid = false
        }

        if (state.answerOptions.isEmpty() || state.answerOptions.all { it.answer.isBlank() }) {
            _uiState.update { it.copy(answersError = "Pelo menos uma resposta deve ser fornecida") }
            isValid = false
        } else if (state.cardType != FlashcardType.BASIC && state.answerOptions.none { it.isCorrect }) {
            _uiState.update { it.copy(answersError = "Pelo menos uma resposta deve estar correta") }
            isValid = false
        }

        return isValid
    }

    fun saveFlashcard(): Boolean {
        viewModelScope.launch {
            if (saveFlashcardSuspending()) {
                _uiState.update { it.copy(isSaved = true) }
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
        
        _uiState.update { it.copy(isLoading = true) }
        return try {
            repository.saveFlashcard(flashcardToSave)
            val isFirstSave = !currentState.isEditMode && !currentState.isNewlyCreated
            _uiState.update { 
                it.copy(
                    isLoading = false, 
                    errorMessage = null, 
                    wasEdited = false,
                    isNewlyCreated = it.isNewlyCreated || isFirstSave
                ) 
            }
            true
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao salvar flashcard: ${e.message}") }
            false
        }
    }

    fun deleteFlashcard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.deleteFlashcard(_uiState.value.flashcardId)
                _uiState.update { it.copy(showDeleteDialog = false, isSaved = true, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao excluir flashcard: ${e.message}") }
            }
        }
    }

    fun discardFlashcard() {
        val currentState = _uiState.value
        if (currentState.isNewlyCreated && !currentState.isSaved) {
            viewModelScope.launch {
                try {
                    repository.deleteFlashcard(currentState.flashcardId)
                } catch (_: Exception) {}
            }
        }
    }
}
