package com.example.cognilink.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cognilink.data.repository.AuthRepository
import com.example.cognilink.data.repository.AuthRepositoryImpl
import com.example.cognilink.ui.states.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onModeChange(isSignUp: Boolean) {
        _uiState.update { it.copy(isSignUpMode = isSignUp) }
    }

    // Sign In Events
    fun onSignInEmailChange(newValue: String) {
        _uiState.update { it.copy(signInEmail = newValue) }
    }

    fun onSignInPasswordChange(newValue: String) {
        _uiState.update { it.copy(signInPassword = newValue) }
    }

    fun onSignInClick() {
        viewModelScope.launch {
            val user = repository.signIn(_uiState.value.signInEmail, _uiState.value.signInPassword)
            if (user != null) {
                _uiState.update { it.copy(loggedInUserId = user.id) }
            } else {
                _uiState.update { it.copy(errorMessage = "Invalid email or password") }
            }
        }
    }

    // Sign Up Events
    fun onSignUpNameChange(newValue: String) {
        _uiState.update { it.copy(signUpName = newValue) }
    }

    fun onSignUpEmailChange(newValue: String) {
        _uiState.update { it.copy(signUpEmail = newValue) }
    }

    fun onSignUpPasswordChange(newValue: String) {
        _uiState.update { it.copy(signUpPassword = newValue) }
    }

    fun onSignUpConfirmPasswordChange(newValue: String) {
        _uiState.update { it.copy(signUpConfirmPassword = newValue) }
    }

    fun onTermsAcceptedChange(newValue: Boolean) {
        _uiState.update { it.copy(isTermsAccepted = newValue) }
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            val user = repository.signUp(
                _uiState.value.signUpName,
                _uiState.value.signUpEmail,
                _uiState.value.signUpPassword
            )
            if (user != null) {
                _uiState.update { it.copy(loggedInUserId = user.id) }
            } else {
                _uiState.update { it.copy(errorMessage = "Registration failed. Please try again.") }
            }
        }
    }

    fun clearNavigationEvent() {
        _uiState.update { it.copy(loggedInUserId = null) }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
