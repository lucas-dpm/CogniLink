package com.example.cognilink.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cognilink.domain.Deck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DeckUiState(
    val currentDeck: Deck? = null
)

class DeckViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DeckUiState())
    val uiState: StateFlow<DeckUiState> = _uiState.asStateFlow()

    fun updateDeck(deck: Deck) {
        _uiState.update { it.copy(currentDeck = deck) }
    }
}
