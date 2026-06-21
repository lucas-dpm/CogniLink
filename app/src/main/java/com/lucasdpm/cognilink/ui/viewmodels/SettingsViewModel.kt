package com.lucasdpm.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.StudyContext
import com.lucasdpm.cognilink.data.repository.AuthRepository
import com.lucasdpm.cognilink.data.repository.StudyContextRepository
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.ui.states.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val contextRepository: StudyContextRepository,
    private val notificationService: AppNotificationService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun loadContexts(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            contextRepository.getContextsForUser(userId).collect { contexts ->
                _uiState.update { it.copy(contexts = contexts, isLoading = false) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update { it.copy(isLoggedOut = true) }
        }
    }

    fun deleteContext(context: StudyContext) {
        viewModelScope.launch {
            contextRepository.deleteContext(context)
            notificationService.showSuccess("Contexto excluído com sucesso")
        }
    }
}
