package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.repository.DeckRepository
import com.example.cognilink.data.repository.UserRepository
import com.example.cognilink.ui.states.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val userRepository: UserRepository,
    private val deckRepository: DeckRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun initialize(userId: Long) {
        if (_uiState.value.userId != 0L) return
        loadHomeData(userId)
    }

    private fun loadHomeData(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val user = userRepository.getUserById(userId = userId)
                val decks = deckRepository.getDecks(userId = userId)
                _uiState.update {
                    it.copy(
                        userId = user.id,
                        userName = user.name,
                        decks = decks,
                        totalStudyTime = user.stats.totalStudyTime,
                        overallMastery = user.stats.overallMastery,
                        cardsDone = user.stats.totalFlashcardsDone,
                        retentionRate = user.stats.retentionRate,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao carregar dados da Home"
                    )
                }
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

    fun onSearchValueChange(newValue: String) {
        _uiState.update { it.copy(searchInput = newValue) }
        filterDecks(newValue)
    }

    private fun filterDecks(query: String) {
        viewModelScope.launch {
            val userId = _uiState.value.userId
            val allDecks = deckRepository.getDecks(userId)
            val filteredDecks = if (query.isEmpty()) {
                allDecks
            } else {
                allDecks.filter { it.name.contains(query, ignoreCase = true) }
            }
            _uiState.update { it.copy(decks = filteredDecks) }
        }
    }

    fun refreshDecks() {
        viewModelScope.launch {
            try {
                val userId = _uiState.value.userId
                val decks = deckRepository.getDecks(userId)
                _uiState.update { it.copy(decks = decks) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao atualizar baralhos") }
            }
        }
    }
}