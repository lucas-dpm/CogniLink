package com.example.cognilink.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.ui.components.auth.AuthFooter
import com.example.cognilink.ui.components.auth.AuthHeader
import com.example.cognilink.ui.components.auth.SignInContent
import com.example.cognilink.ui.components.auth.SignUpContent
import com.example.cognilink.ui.theme.*
import com.example.cognilink.ui.states.AuthUiState
import com.example.cognilink.ui.viewmodels.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToTerms: () -> Unit = {},
    onNavigateToHome: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Observa o sucesso do login para navegar
    LaunchedEffect(uiState.loggedInUserId) {
        uiState.loggedInUserId?.let { userId ->
            onNavigateToHome(userId)
            viewModel.clearNavigationEvent() // Limpa para evitar re-navegação ao voltar
        }
    }
    
    // Exibe erro se houver
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            // Aqui poderia ser um SnackbarHostState.showSnackbar
        }
    }

    AuthContent(
        uiState = uiState,
        onModeChange = viewModel::onModeChange,
        onSignInEmailChange = viewModel::onSignInEmailChange,
        onSignInPasswordChange = viewModel::onSignInPasswordChange,
        onSignInClick = {
            viewModel.onSignInClick()
        },
        onSignUpNameChange = viewModel::onSignUpNameChange,
        onSignUpEmailChange = viewModel::onSignUpEmailChange,
        onSignUpPasswordChange = viewModel::onSignUpPasswordChange,
        onSignUpConfirmPasswordChange = viewModel::onSignUpConfirmPasswordChange,
        onTermsAcceptedChange = viewModel::onTermsAcceptedChange,
        onSignUpClick = {
            viewModel.onSignUpClick()
        },
        onNavigateToTerms = onNavigateToTerms
    )
}

@Composable
fun AuthContent(
    uiState: AuthUiState,
    onModeChange: (Boolean) -> Unit,
    onSignInEmailChange: (String) -> Unit,
    onSignInPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onSignUpNameChange: (String) -> Unit,
    onSignUpEmailChange: (String) -> Unit,
    onSignUpPasswordChange: (String) -> Unit,
    onSignUpConfirmPasswordChange: (String) -> Unit,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onSignUpClick: () -> Unit,
    onNavigateToTerms: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = OffWhite),
    ) {
        AuthHeader()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-30).dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = VeryLightGray,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { onModeChange(false) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!uiState.isSignUpMode) Color.White else Color.Transparent
                        )
                    ) {
                        Text(
                            "ENTRAR",
                            fontWeight = FontWeight.Bold,
                            color = if (!uiState.isSignUpMode) DarkNavyBlue else DarkGray
                        )
                    }
                    Button(
                        onClick = { onModeChange(true) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (uiState.isSignUpMode) Color.White else Color.Transparent
                        )
                    ) {
                        Text(
                            "CADASTRAR",
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.isSignUpMode) DarkNavyBlue else DarkGray
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                if (uiState.isSignUpMode) {
                    SignUpContent(
                        name = uiState.signUpName,
                        onNameChange = onSignUpNameChange,
                        email = uiState.signUpEmail,
                        onEmailChange = onSignUpEmailChange,
                        password = uiState.signUpPassword,
                        onPasswordChange = onSignUpPasswordChange,
                        confirmPassword = uiState.signUpConfirmPassword,
                        onConfirmPasswordChange = onSignUpConfirmPasswordChange,
                        isTermsAccepted = uiState.isTermsAccepted,
                        onTermsAcceptedChange = onTermsAcceptedChange,
                        onSignUpClick = onSignUpClick,
                        onTermsClick = onNavigateToTerms
                    )
                } else {
                    SignInContent(
                        email = uiState.signInEmail,
                        onEmailChange = onSignInEmailChange,
                        password = uiState.signInPassword,
                        onPasswordChange = onSignInPasswordChange,
                        onSignInClick = onSignInClick,
                        onSignUpClick = { onModeChange(true) }
                    )
                }
            }
        }
        AuthFooter(onTermsClick = onNavigateToTerms)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthScreenPreview() {
    CogniLinkTheme {
        var state by remember { mutableStateOf(AuthUiState()) }
        
        AuthContent(
            uiState = state,
            onModeChange = { state = state.copy(isSignUpMode = it) },
            onSignInEmailChange = { state = state.copy(signInEmail = it) },
            onSignInPasswordChange = { state = state.copy(signInPassword = it) },
            onSignInClick = { /* No-op in preview */ },
            onSignUpNameChange = { state = state.copy(signUpName = it) },
            onSignUpEmailChange = { state = state.copy(signUpEmail = it) },
            onSignUpPasswordChange = { state = state.copy(signUpPassword = it) },
            onSignUpConfirmPasswordChange = { state = state.copy(signUpConfirmPassword = it) },
            onTermsAcceptedChange = { state = state.copy(isTermsAccepted = it) },
            onSignUpClick = { /* No-op in preview */ }
        )
    }
}
