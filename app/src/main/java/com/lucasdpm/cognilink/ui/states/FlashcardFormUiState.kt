package com.lucasdpm.cognilink.ui.states

import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import java.util.UUID

data class FlashcardFormUiState(
    val deckId: String? = null,
    val flashcardId: String = UUID.randomUUID().toString(),
    val questionText: String = "",
    val cardType: FlashcardType = FlashcardType.BASIC,
    val difficulty: DifficultyLevel = DifficultyLevel.EASY,
    val answerOptions: List<Answer> = emptyList(),
    val hints: List<String> = emptyList(),
    val isDeleteMode: Boolean = false,
    val isInitialized: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val isEditMode: Boolean = false,
    val isNewlyCreated: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showChangeDialog: Boolean = false,
    val wasEdited: Boolean = false,
    val isMenuExpanded: Boolean = false,
    val questionTextError: String? = null,
    val answersError: String? = null,
)
