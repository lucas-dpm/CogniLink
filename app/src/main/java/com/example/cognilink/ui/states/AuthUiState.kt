package com.example.cognilink.ui.states

data class AuthUiState(
    val isSignUpMode: Boolean = false,
    val signInEmail: String = "",
    val signInPassword: String = "",
    val signUpName: String = "",
    val signUpEmail: String = "",
    val signUpPassword: String = "",
    val signUpConfirmPassword: String = "",
    val isTermsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loggedInUserId: Long? = null
)
