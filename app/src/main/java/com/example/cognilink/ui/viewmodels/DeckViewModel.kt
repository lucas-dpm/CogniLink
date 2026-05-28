package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.model.Deck
import com.example.cognilink.data.repository.DeckRepository
import com.example.cognilink.data.repository.DeckRepositoryImpl
import com.example.cognilink.data.repository.FlashcardRepository
import com.example.cognilink.data.repository.FlashcardRepositoryImpl
import com.example.cognilink.data.repository.UserRepository
import com.example.cognilink.data.repository.UserRepositoryImpl
import com.example.cognilink.ui.states.DeckUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckViewModel(
    private val deckRepository: DeckRepository = DeckRepositoryImpl(),
    private val userRepository: UserRepository = UserRepositoryImpl(),
    private val flashcardRepository: FlashcardRepository = FlashcardRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeckUiState())
    val uiState: StateFlow<DeckUiState> = _uiState.asStateFlow()

    fun initialize(deckId: Long, userId: Long) {
        if (_uiState.value.currentDeck != null) return
        loadDeckDetails(deckId, userId)
    }

    private fun loadDeckDetails(deckId: Long,userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val deck = deckRepository.getDeckById(deckId, userId)
                
                if (deck != null) {
                    _uiState.update { it.copy(currentDeck = deck) }
                    loadFlashcards(deck.id)
                } else {
                    _uiState.update { it.copy(errorMessage = "Baralho não encontrado") }
                }
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao carregar baralho") }
            }
        }
    }

    private fun loadFlashcards(deckId: Long) {
        viewModelScope.launch {
            try {
                val flashcards = flashcardRepository.getFlashcardsForDeck(deckId)
                _uiState.update { it.copy(flashcards = flashcards) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Erro ao carregar flashcards") }
            }
        }
    }

    fun updateDeck(deck: Deck, userId: Long) {
        _uiState.update { it.copy(currentDeck = deck) }
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            deckRepository.saveDeck(deck, user.id)
        }
    }

    fun deleteDeck(deckId: Long, userId: Long) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            deckRepository.deleteDeck(deckId, user.id)
        }
    }

    fun addDeck(deck: Deck, userId: Long) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            deckRepository.saveDeck(deck, user.id)
        }
    }



}
