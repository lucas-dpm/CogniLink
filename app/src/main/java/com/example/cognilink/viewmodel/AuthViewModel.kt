package com.example.cognilink.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AuthUiState(
    val isSignUpMode: Boolean = false,
    val signInEmail: String = "",
    val signInPassword: String = "",
    val signUpName: String = "",
    val signUpEmail: String = "",
    val signUpPassword: String = "",
    val signUpConfirmPassword: String = "",
    val isTermsAccepted: Boolean = false
)

class AuthViewModel : ViewModel() {

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
        // TODO: Implement Login Logic
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
        // TODO: Implement Registration Logic
    }
}
