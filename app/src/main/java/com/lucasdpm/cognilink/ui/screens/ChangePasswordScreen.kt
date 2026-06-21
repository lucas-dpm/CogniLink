package com.lucasdpm.cognilink.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.input.PasswordTextField
import com.lucasdpm.cognilink.ui.components.utils.NavigationHeader
import com.lucasdpm.cognilink.ui.components.utils.buttons.NeonActionButton
import com.lucasdpm.cognilink.ui.components.utils.labels.CustomLabel
import com.lucasdpm.cognilink.ui.states.ChangePasswordUiState
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.viewmodels.ChangePasswordViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChangePasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: ChangePasswordViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isPasswordChanged) {
        if (uiState.isPasswordChanged) {
            onNavigateBack()
        }
    }

    ChangePasswordScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onSaveClick = viewModel::changePassword
    )
}

@Composable
fun ChangePasswordScreenContent(
    uiState: ChangePasswordUiState,
    onNavigateBack: () -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        topBar = {
            NavigationHeader(
                title = "Alterar Senha",
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                NeonActionButton(
                    text = "SALVAR NOVA SENHA",
                    icon = R.drawable.ic_check_circle,
                    onClickButton = onSaveClick,
                    isEnabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        },
        containerColor = OffWhite,
        modifier = Modifier.statusBarsPadding()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomLabel(text = "NOVA SENHA")
                PasswordTextField(
                    password = uiState.newPassword,
                    onPasswordChange = onNewPasswordChange,
                    errorMessage = uiState.passwordError
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomLabel(text = "CONFIRMAR NOVA SENHA")
                PasswordTextField(
                    password = uiState.confirmPassword,
                    onPasswordChange = onConfirmPasswordChange,
                    errorMessage = uiState.confirmPasswordError
                )
            }
        }
    }
}
