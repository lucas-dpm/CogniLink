package com.lucasdpm.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.UserStats
import com.lucasdpm.cognilink.data.repository.UserRepository
import com.lucasdpm.cognilink.domain.usecase.CalculateUserRankingUseCase
import com.lucasdpm.cognilink.ui.states.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

class ProfileViewModel(
    private val calculateUserRankingUseCase: CalculateUserRankingUseCase,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun initialize(userId: String) {
        val currentState = _uiState.value
        if (currentState is ProfileUiState.Success && currentState.userId == userId) return
        loadUserProfileData(userId)
    }

    private fun loadUserProfileData(userId: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val user = userRepository.getUserById(userId)
                if (user == null) {
                    _uiState.value = ProfileUiState.Error("Usuário não encontrado")
                    return@launch
                }
                
                userRepository.getUserStats(userId).collect { stats ->
                    val userStats = stats ?: UserStats(userId = userId)
                    val rankingResult = calculateUserRankingUseCase(userStats)

                    _uiState.value = ProfileUiState.Success(
                        userId = userId,
                        userName = user.name,
                        stats = userStats,
                        ranking = rankingResult
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun formatTime(milliseconds: Long): String {
        if (milliseconds <= 0) return "0s"

        val totalDays = TimeUnit.MILLISECONDS.toDays(milliseconds)
        val years = totalDays / 365
        val days = totalDays % 365
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60

        val parts = mutableListOf<String>()
        if (years > 0) parts.add("${years}y")
        if (days > 0) parts.add("${days}d")
        if (hours > 0) parts.add("${hours}h")
        if (minutes > 0) parts.add("${minutes}m")
        if (seconds > 0 || parts.isEmpty()) parts.add("${seconds}s")

        return parts.joinToString(" ")
    }

    fun formatLastReview(lastReviewTimestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - lastReviewTimestamp
        if (diff < 0) return "Agora mesmo"

        val totalDays = TimeUnit.MILLISECONDS.toDays(diff)
        val years = totalDays / 365
        val days = totalDays % 365
        val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60

        return when {
            years > 0 -> "$years anos atrás Última Revisão"
            days > 0 -> "$days dias atrás Última Revisão"
            hours > 0 -> "${hours}h atrás Última Revisão"
            minutes > 0 -> "${minutes}min atrás Última Revisão"
            else -> "Agora mesmo"
        }
    }

    fun formatLatency(latencyMs: Long): String {
        return String.format(Locale.getDefault(), "%.1f ms", latencyMs.toDouble())
    }
}
