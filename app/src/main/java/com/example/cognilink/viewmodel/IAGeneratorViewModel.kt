package com.example.cognilink.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cognilink.domain.DifficultyLevel
import com.example.cognilink.domain.FlashcardType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.enums.enumEntries

data class IAGeneratorUiState(
    val flashcardTheme: String = "",
    val quantity: Int = 1,
    val selectedDifficulty: DifficultyLevel? = null,
    val typeOptions: List<FlashcardType> = enumEntries<FlashcardType>(),
    val selectedType: FlashcardType? = null,
    val isLoading: Boolean = false
)

class IAGeneratorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(IAGeneratorUiState())
    val uiState: StateFlow<IAGeneratorUiState> = _uiState.asStateFlow()

    fun onThemeChange(newTheme: String) {
        _uiState.update { it.copy(flashcardTheme = newTheme) }
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

    fun generateFlashcards() {
        _uiState.update { it.copy(isLoading = true) }
        // TODO: Lógica para chamar a IA
        // viewModelScope.launch { ... }
    }
}
