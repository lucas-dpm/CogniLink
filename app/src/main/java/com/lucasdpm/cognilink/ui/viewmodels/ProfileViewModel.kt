package com.lucasdpm.cognilink.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.UserStats
import com.lucasdpm.cognilink.data.repository.UserRepository
import com.lucasdpm.cognilink.domain.usecase.CalculateUserRankingUseCase
import com.lucasdpm.cognilink.ui.states.ProfileUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
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

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var profileJob: Job? = null

    fun initialize(userId: String) {
        val currentState = _uiState.value
        if (currentState is ProfileUiState.Success && currentState.userId == userId) return
        
        profileJob?.cancel()
        profileJob = loadUserProfileData(userId)
    }

    private fun loadUserProfileData(userId: String): Job {
        return viewModelScope.launch {
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
                if (e is CancellationException) throw e
                _uiState.value = ProfileUiState.Error("Erro ao carregar perfil")
                Log.e(TAG, "loadUserProfileData: Erro ao carregar dados do perfil", e)
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
        if (lastReviewTimestamp <= 0L) return "Nenhuma revisão realizada"

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
        if (latencyMs < 1000) {
            return String.format(Locale.getDefault(), "%.1f ms", latencyMs.toDouble())
        }

        val seconds = TimeUnit.MILLISECONDS.toSeconds(latencyMs)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(latencyMs)
        val hours = TimeUnit.MILLISECONDS.toHours(latencyMs)

        return when {
            hours > 0 -> String.format(Locale.getDefault(), "%dh %dm %ds", hours, minutes % 60, seconds % 60)
            minutes > 0 -> String.format(Locale.getDefault(), "%dm %ds", minutes, seconds % 60)
            else -> String.format(Locale.getDefault(), "%.1f s", latencyMs / 1000.0)
        }
    }
}
