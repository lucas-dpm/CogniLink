package com.lucasdpm.cognilink.ui.states

import com.lucasdpm.cognilink.data.model.FlashcardWithStats

data class DeckFormUiState(
    val userId: String? = null,
    val deckId: String = "",
    val deckName: String = "",
    val deckDescription: String = "",
    val deckCategories: List<String> = emptyList(),
    val deckFlashcards: List<FlashcardWithStats> = emptyList(),
    val showCategoryDialog: Boolean = false,
    val categoryBeingEdited: String? = null,
    val categoryText: String = "",
    val isRemoveMode: Boolean = false,
    val showAddFlashcardDialog: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val isEditMode: Boolean = false,
    val wasEdited: Boolean = false,
    val showChangeDialog: Boolean = false,
    val deckNameError: String? = null,
)