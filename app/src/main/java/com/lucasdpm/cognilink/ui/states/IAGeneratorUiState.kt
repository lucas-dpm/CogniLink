package com.lucasdpm.cognilink.ui.states

import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import kotlin.enums.enumEntries

data class IAGeneratorUiState(
    val deckId: String? = null,
    val flashcardTheme: String = "",
    val quantity: Int = 1,
    val selectedDifficulty: DifficultyLevel? = null,
    val typeOptions: List<FlashcardType> = enumEntries<FlashcardType>(),
    val selectedType: FlashcardType? = null,
    val hasFile: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val themeError: String? = null
)
