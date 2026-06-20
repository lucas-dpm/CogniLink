package com.lucasdpm.cognilink.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.UserStats
import com.lucasdpm.cognilink.data.repository.DeckRepository
import com.lucasdpm.cognilink.data.repository.UserRepository
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.ui.states.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val userRepository: UserRepository,
    private val deckRepository: DeckRepository,
    private val notificationService: AppNotificationService
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun initialize(userId: String) {
        if (_uiState.value.userId == userId) return
        _uiState.update { it.copy(userId = userId) }
        loadHomeData()
    }

    private fun loadHomeData() {
        val currentState = _uiState.value
        val userId = currentState.userId ?: return

        // 1. Observar baralhos
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDecks = true) }
            try {
                deckRepository.getDecks(userId).collect { decks ->
                    _uiState.update {
                        it.copy(
                            decks = decks,
                            isLoadingDecks = false
                        )
                    }
                    filterDecks(_uiState.value.searchInput)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingDecks = false) }
                notificationService.showError("Erro ao carregar baralhos")
                Log.e(TAG, "loadHomeData (decks): Erro ao carregar baralhos", e)
            }
        }

        // 2. Observar estatísticas
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingStats = true) }
            try {
                userRepository.getUserStats(userId).collect { stats ->
                    val userStats = stats ?: UserStats(userId = userId)
                    _uiState.update {
                        it.copy(
                            totalStudyTime = userStats.totalStudyTime,
                            overallMastery = userStats.overallMastery,
                            cardsDone = userStats.totalFlashcardsDone,
                            retentionRate = userStats.retentionRate,
                            isLoadingStats = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingStats = false) }
                notificationService.showWarning("Erro ao carregar estatísticas")
                Log.e(TAG, "loadHomeData (stats): Erro ao carregar estatísticas", e)
            }
        }

        // 3. Carregar Perfil (Nome)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingUser = true) }
            try {
                val user = userRepository.getUserById(userId = userId)
                if (user == null) {
                    _uiState.update {
                        it.copy(
                            isLoadingUser = false,
                            errorMessage = "Usuário não encontrado. Por favor, faça login novamente.",
                            showCriticalErrorDialog = true
                        )
                    }
                    return@launch
                }

                _uiState.update {
                    it.copy(
                        userName = user.name,
                        isLoadingUser = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingUser = false,
                        errorMessage = "Falha ao conectar com o servidor.",
                        showCriticalErrorDialog = true
                    )
                }
                Log.e(TAG, "loadHomeData (user): Falha ao conectar com o servidor", e)
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

        return when {
            years > 0 -> "${years}y"
            days > 0 -> "${days}d"
            hours > 0 -> "${hours}h"
            minutes > 0 -> "${minutes}m"
            else -> "${seconds}s"
        }
    }

    fun onSearchValueChange(newValue: String) {
        _uiState.update { it.copy(searchInput = newValue) }
        filterDecks(newValue)
    }

    private fun filterDecks(query: String) {
        val filteredList = if (query.isEmpty()) {
            _uiState.value.decks
        } else {
            _uiState.value.decks.filter { it.name.contains(query, ignoreCase = true) }
        }
        _uiState.update { it.copy(filteredDecks = filteredList) }
    }
}
