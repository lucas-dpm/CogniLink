package com.lucasdpm.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.repository.AuthRepository
import com.lucasdpm.cognilink.ui.states.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onModeChange(isSignUp: Boolean) {
        _uiState.update { it.copy(isSignUpMode = isSignUp) }
    }

    // Sign In Events
    fun onSignInEmailChange(newValue: String) {
        _uiState.update { it.copy(signInEmail = newValue, signInEmailError = null) }
    }

    fun onSignInPasswordChange(newValue: String) {
        _uiState.update { it.copy(signInPassword = newValue, signInPasswordError = null) }
    }

    private fun validateSignIn(): Boolean {
        val email = _uiState.value.signInEmail
        val password = _uiState.value.signInPassword
        var isValid = true

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(signInEmailError = "E-mail inválido") }
            isValid = false
        }

        if (password.isBlank()) {
            _uiState.update { it.copy(signInPasswordError = "A senha não pode estar vazia") }
            isValid = false
        }

        return isValid
    }

    fun onSignInClick() {
        if (!validateSignIn()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val user = repository.signIn(_uiState.value.signInEmail)
            if (user != null) {
                _uiState.update { it.copy(loggedInUserId = user.id, isLoading = false) }
            } else {
                _uiState.update { it.copy(errorMessage = "E-mail ou senha incorretos", isLoading = false) }
            }
        }
    }

    // Sign Up Events
    fun onSignUpNameChange(newValue: String) {
        _uiState.update { it.copy(signUpName = newValue, nameError = null) }
    }

    fun onSignUpEmailChange(newValue: String) {
        _uiState.update { it.copy(signUpEmail = newValue, signUpEmailError = null) }
    }

    fun onSignUpPasswordChange(newValue: String) {
        _uiState.update { it.copy(signUpPassword = newValue, signUpPasswordError = null) }
    }

    fun onSignUpConfirmPasswordChange(newValue: String) {
        _uiState.update { it.copy(signUpConfirmPassword = newValue, confirmPasswordError = null) }
    }

    private fun validateSignUp(): Boolean {
        val state = _uiState.value
        var isValid = true

        if (state.signUpName.isBlank()) {
            _uiState.update { it.copy(nameError = "O nome é obrigatório") }
            isValid = false
        }

        if (state.signUpEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(state.signUpEmail).matches()) {
            _uiState.update { it.copy(signUpEmailError = "E-mail inválido") }
            isValid = false
        }

        if (state.signUpPassword.length < 6) {
            _uiState.update { it.copy(signUpPasswordError = "A senha deve ter pelo menos 6 caracteres") }
            isValid = false
        }

        if (state.signUpPassword != state.signUpConfirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "As senhas não coincidem") }
            isValid = false
        }

        if (!state.isTermsAccepted) {
            _uiState.update { it.copy(errorMessage = "Você deve aceitar os termos de uso") }
            isValid = false
        }

        return isValid
    }

    fun onSignUpClick() {
        if (!validateSignUp()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val user = repository.signUp(
                _uiState.value.signUpName,
                _uiState.value.signUpEmail
            )
            if (user != null) {
                _uiState.update { it.copy(loggedInUserId = user.id, isLoading = false) }
            } else {
                _uiState.update { it.copy(errorMessage = "Falha no cadastro. Tente novamente.", isLoading = false) }
            }
        }
    }

    fun onTermsAcceptedChange(accepted: Boolean) {
        _uiState.update { it.copy(isTermsAccepted = accepted) }
    }

    fun clearNavigationEvent() {
        _uiState.update { it.copy(loggedInUserId = null) }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
