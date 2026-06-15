package com.lucasdpm.cognilink.ui.states

import com.lucasdpm.cognilink.data.model.Deck

data class HomeUiState(
    val userId: String? = null,
    val userName: String = "",
    val welcomePhrase: String = "Pronto para subir de nível no seu conhecimento hoje?",
    val overallMastery: Float = 0f,
    val totalStudyTime: Long = 0L,
    val cardsDone: Int = 0,
    val retentionRate: Float = 0f,
    val searchInput: String = "",
    val filteredDecks: List<Deck> = emptyList(),
    val decks: List<Deck> = emptyList(),
    
    // Flags individuais para Shimmer parcial
    val isLoadingUser: Boolean = true,
    val isLoadingDecks: Boolean = true,
    val isLoadingStats: Boolean = true,

    val snackbarMessage: SnackbarMessage? = null,
    val showCriticalErrorDialog: Boolean = false,
    val errorMessage: String? = null
)
