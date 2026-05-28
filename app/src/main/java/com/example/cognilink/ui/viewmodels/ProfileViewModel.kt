package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.model.UserStats
import com.example.cognilink.data.repository.UserRepository
import com.example.cognilink.data.repository.UserRepositoryImpl
import com.example.cognilink.domain.model.UserRankingResult
import com.example.cognilink.domain.usecase.CalculateUserRankingUseCase
import com.example.cognilink.ui.states.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

class ProfileViewModel(
    private val calculateUserRankingUseCase: CalculateUserRankingUseCase = CalculateUserRankingUseCase(),
    private val repository: UserRepository = UserRepositoryImpl()
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun initialize(userId: Long) {
        loadUserProfileData(userId)
    }

    private fun loadUserProfileData(userId: Long) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val user = repository.getUserById(userId)
                val userStats = user.stats
                val rankingResult = calculateUserRankingUseCase(userStats)

                _uiState.value = ProfileUiState.Success(
                    userName = user.name,
                    stats = userStats,
                    ranking = rankingResult
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun formatTime(milliseconds: Long): String {
        if (milliseconds <= 0) return "0s"

        val days = TimeUnit.MILLISECONDS.toDays(milliseconds)
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60

        val parts = mutableListOf<String>()
        if (days > 0) parts.add("${days}d")
        if (hours > 0) parts.add("${hours}h")
        if (minutes > 0) parts.add("${minutes}m")
        if (seconds > 0) parts.add("${seconds}s")

        return parts.joinToString(" ")
    }

    fun formatLastReview(lastReviewTimestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - lastReviewTimestamp
        if (diff < 0) return "Agora mesmo"

        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60

        return when {
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
