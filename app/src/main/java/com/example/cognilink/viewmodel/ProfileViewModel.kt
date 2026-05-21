package com.example.cognilink.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cognilink.domain.UserStats
import com.example.cognilink.domain.fakeUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val userName: String = "",
    val userRank: String = "",
    val userStats: UserStats? = null,
    val cognitiveEfficiencyText: String = "Seu cérebro está absorvendo mais conteúdo em menos tempo."
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // Inicializa com dados fake por enquanto
        _uiState.value = ProfileUiState(
            userName = fakeUser.name,
            userRank = "Iniciante",
            userStats = fakeUser.stats
        )
    }
}
