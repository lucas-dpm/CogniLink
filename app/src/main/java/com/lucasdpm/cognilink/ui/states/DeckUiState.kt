package com.lucasdpm.cognilink.ui.states

import com.lucasdpm.cognilink.data.model.Deck
import com.lucasdpm.cognilink.data.model.FlashcardWithStats

data class DeckUiState(
    val userId: String? = null,
    val deckId: String? = null,
    val currentDeck: Deck? = null,
    val flashcards: List<FlashcardWithStats> = emptyList(),
    val isMenuExpanded: Boolean = false,
    val isAddFlashcardDialogOpen: Boolean = false,
    val isDeleteDeckDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
