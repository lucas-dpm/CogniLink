package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.model.Deck
import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.repository.DeckRepository
import com.example.cognilink.data.repository.DeckRepositoryImpl
import com.example.cognilink.data.repository.UserRepository
import com.example.cognilink.data.repository.UserRepositoryImpl
import com.example.cognilink.ui.states.DeckEditorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckEditorViewModel(
    private val repository: DeckRepository = DeckRepositoryImpl(),
    private val userRepository: UserRepository = UserRepositoryImpl()
) : ViewModel() {
    private val _uiState = MutableStateFlow(DeckEditorUiState())
    val uiState: StateFlow<DeckEditorUiState> = _uiState.asStateFlow()

    fun toggleRemoveMode() {
        _uiState.update { it.copy(isRemoveMode = !it.isRemoveMode) }
    }

    fun addFlashcard(flashcard: Flashcard) {
        _uiState.update { it.copy(deckFlashcards = it.deckFlashcards + flashcard) }
    }

    fun removeFlashcard(flashcard: Flashcard) {
        _uiState.update { it.copy(deckFlashcards = it.deckFlashcards - flashcard) }
    }

    fun onDeckNameChange(newValue: String) {
        _uiState.update { it.copy(deckName = newValue) }
    }

    fun onDeckDescriptionChange(newValue: String) {
        _uiState.update { it.copy(deckDescription = newValue) }
    }

    fun onCategoryTextChange(newText: String) {
        _uiState.update { it.copy(categoryText = newText) }
    }

    fun openCategoryDialog(category: String? = null) {
        _uiState.update { it.copy(
            categoryBeingEdited = category,
            categoryText = category ?: "",
            showCategoryDialog = true
        ) }
    }

    fun closeCategoryDialog() {
        _uiState.update { it.copy(
            showCategoryDialog = false,
            categoryText = "",
            categoryBeingEdited = null
        ) }
    }

    fun handleCategoryConfirmation() {
        val currentState = _uiState.value
        if (currentState.categoryText.isNotBlank()) {
            val oldName = currentState.categoryBeingEdited
            val newCategories = if (oldName == null) {
                if (!currentState.deckCategories.contains(currentState.categoryText)) {
                    currentState.deckCategories + currentState.categoryText
                } else {
                    currentState.deckCategories
                }
            } else {
                currentState.deckCategories.map { if (it == oldName) currentState.categoryText else it }
            }
            _uiState.update { it.copy(
                deckCategories = newCategories,
                showCategoryDialog = false,
                categoryText = "",
                categoryBeingEdited = null
            ) }
        }
    }

    fun removeCategory(category: String) {
        _uiState.update { it.copy(deckCategories = it.deckCategories - category) }
    }

    fun saveDeck() {
        viewModelScope.launch {
            val user = userRepository.getUserById(1L)
            val currentState = _uiState.value
            val deckToSave = Deck(
                id = 0,
                userId = user.id,
                name = currentState.deckName,
                description = currentState.deckDescription,
                categories = currentState.deckCategories,
                difficulty = com.example.cognilink.domain.model.DifficultyLevel.MEDIUM,
                mastery = 0f,
                totalCards = currentState.deckFlashcards.size,
                cardsToReview = currentState.deckFlashcards.size
            )
            repository.saveDeck(deckToSave, user.id)
        }
    }

    fun loadDeckData(name: String, description: String, categories: List<String>) {
        _uiState.update { it.copy(
            deckName = name,
            deckDescription = description,
            deckCategories = categories
        ) }
    }
}
