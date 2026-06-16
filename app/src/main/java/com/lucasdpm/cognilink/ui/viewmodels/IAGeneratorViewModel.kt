package com.lucasdpm.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.data.model.Flashcard
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.domain.repository.AIService
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.ui.states.IAGeneratorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IAGeneratorViewModel(
    private val aiService: AIService,
    private val flashcardRepository: FlashcardRepository,
    private val notificationService: AppNotificationService
) : ViewModel() {

    private val _uiState = MutableStateFlow(IAGeneratorUiState())
    val uiState: StateFlow<IAGeneratorUiState> = _uiState.asStateFlow()

    fun initialize(deckId: String) {
        if (_uiState.value.deckId == deckId) return
        _uiState.update { it.copy(deckId = deckId) }
    }

    fun onThemeChange(newTheme: String) {
        _uiState.update { it.copy(flashcardTheme = newTheme, themeError = null) }
    }

    fun onQuantityChange(newQuantity: Int) {
        _uiState.update { it.copy(quantity = newQuantity) }
    }

    fun onDifficultyChange(newDifficulty: DifficultyLevel?) {
        _uiState.update { it.copy(selectedDifficulty = newDifficulty) }
    }

    fun onTypeChange(newType: FlashcardType?) {
        _uiState.update { it.copy(selectedType = newType) }
    }

    fun onUploadFile(){
        //TODO
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        if (state.flashcardTheme.isBlank() && !state.hasFile) {
            _uiState.update { it.copy(themeError = "Forneça um tema ou anexe um arquivo") }
            return false
        }
        return true
    }

    fun generateFlashcards() {
        if (!validate()) return

        val state = _uiState.value
        val deckId = state.deckId ?: return
        
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            val result = aiService.generateFlashcards(
                theme = state.flashcardTheme,
                quantity = state.quantity,
                difficulty = state.selectedDifficulty,
                type = state.selectedType
            )

            result.onSuccess { generatedList ->
                val flashcards = generatedList.map { generated ->
                    Flashcard(
                        deckId = deckId,
                        question = generated.question,
                        cardType = state.selectedType ?: FlashcardType.BASIC,
                        difficulty = state.selectedDifficulty ?: DifficultyLevel.MEDIUM,
                        hints = emptyList(),
                        answerOptions = generated.options?.map { 
                            Answer(it, isCorrect = it == generated.answer) 
                        } ?: listOf(Answer(generated.answer, isCorrect = true))
                    )
                }
                
                flashcardRepository.saveAllFlashcards(flashcards)
                _uiState.update { it.copy(isLoading = false) }
                // Navegar de volta ou mostrar sucesso
            }.onFailure { error ->
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = "Erro ao gerar flashcards: ${error.message}"
                    ) 
                }
            }
        }
    }
}
