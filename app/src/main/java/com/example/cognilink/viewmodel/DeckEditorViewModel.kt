package com.example.cognilink.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cognilink.domain.Flashcard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DeckEditorUiState(
    val deckName: String = "",
    val deckDescription: String = "",
    val deckCategories: List<String> = emptyList(),
    val deckFlashcards: List<Flashcard> = emptyList(),
    val showCategoryDialog: Boolean = false,
    val categoryBeingEdited: String? = null,
    val categoryText: String = "",
    val isRemoveMode: Boolean = false
)

class DeckEditorViewModel : ViewModel() {
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
        // TODO: Implementar persistência via Repository
    }

    fun loadDeckData(name: String, description: String, categories: List<String>) {
        _uiState.update { it.copy(
            deckName = name,
            deckDescription = description,
            deckCategories = categories
        ) }
    }
}
