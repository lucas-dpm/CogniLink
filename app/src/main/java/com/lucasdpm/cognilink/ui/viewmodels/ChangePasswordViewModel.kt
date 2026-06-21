package com.lucasdpm.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.repository.AuthRepository
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.ui.states.ChangePasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val authRepository: AuthRepository,
    private val notificationService: AppNotificationService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun onNewPasswordChange(password: String) {
        _uiState.update { it.copy(newPassword = password, passwordError = null) }
    }

    fun onConfirmPasswordChange(password: String) {
        _uiState.update { it.copy(confirmPassword = password, confirmPasswordError = null) }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        var isValid = true

        val passwordError = when {
            state.newPassword.isEmpty() -> "A senha não pode ser vazia"
            state.newPassword.length < 6 -> "A senha deve ter pelo menos 6 caracteres"
            else -> null
        }

        val confirmPasswordError = when {
            state.confirmPassword.isEmpty() -> "Confirme sua senha"
            state.confirmPassword != state.newPassword -> "As senhas não coincidem"
            else -> null
        }

        if (passwordError != null || confirmPasswordError != null) {
            _uiState.update { 
                it.copy(
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            isValid = false
        }

        return isValid
    }

    fun changePassword() {
        if (!validate()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = authRepository.changePassword(_uiState.value.newPassword)
            if (success) {
                notificationService.showSuccess("Senha alterada com sucesso")
                _uiState.update { it.copy(isPasswordChanged = true, isLoading = false) }
            } else {
                notificationService.showError("Erro ao alterar senha")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
