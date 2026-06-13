package com.lucasdpm.cognilink.ui.states

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
    val errorMessage: String? = null, // Erro geral (ex: falha na rede)
    val loggedInUserId: String? = null,
    
    // Erros específicos de campos
    val signUpEmailError: String? = null,
    val signInEmailError: String? = null,
    val signUpPasswordError: String? = null,
    val signInPasswordError: String? = null,
    val nameError: String? = null,
    val confirmPasswordError: String? = null
)
