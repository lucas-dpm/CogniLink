package com.lucasdpm.cognilink.ui.states

data class ChangePasswordUiState(
    val oobCode: String? = null,
    val newPassword: String = "",
    val confirmPassword: String = "",
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val isPasswordChanged: Boolean = false
)
