package com.lucasdpm.cognilink.ui.states

data class ContextFormUiState(
    val id: String? = null,
    val userId: String = "",
    val deckId: String? = null,
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val radius: Float = 50f,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val nameError: String? = null,
    val initialLocationLoaded: Boolean = false
)
