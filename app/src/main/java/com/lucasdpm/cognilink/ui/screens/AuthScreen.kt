package com.lucasdpm.cognilink.ui.screens

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
import com.lucasdpm.cognilink.ui.components.auth.AuthFooter
import com.lucasdpm.cognilink.ui.components.auth.AuthHeader
import com.lucasdpm.cognilink.ui.components.auth.SignInContent
import com.lucasdpm.cognilink.ui.components.auth.SignUpContent
import com.lucasdpm.cognilink.ui.theme.*
import com.lucasdpm.cognilink.ui.states.AuthUiState
import com.lucasdpm.cognilink.ui.viewmodels.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onNavigateToTerms: () -> Unit = {},
    onNavigateToHome: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Observa o sucesso do login para navegar
    LaunchedEffect(uiState.loggedInUserId) {
        uiState.loggedInUserId?.let { userId ->
            onNavigateToHome(userId)
            viewModel.clearNavigationEvent() // Limpa para evitar renavegarão ao voltar
        }
    }
    
    // Exibe erro se houver
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            // Aqui poderia ser um 'SnackbarHostState.showSnackbar'
        }
    }

    AuthContent(
        isSignUpMode = uiState.isSignUpMode,
        signInEmail = uiState.signInEmail,
        signInPassword = uiState.signInPassword,
        signUpName = uiState.signUpName,
        signUpEmail = uiState.signUpEmail,
        signUpPassword = uiState.signUpPassword,
        signUpConfirmPassword = uiState.signUpConfirmPassword,
        isTermsAccepted = uiState.isTermsAccepted,
        signInEmailError = uiState.signInEmailError,
        signUpEmailError = uiState.signUpEmailError,
        signInPasswordError = uiState.signInPasswordError,
        signUpPasswordError = uiState.signUpPasswordError,
        nameError = uiState.nameError,
        confirmPasswordError = uiState.confirmPasswordError,
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
    isSignUpMode: Boolean,
    signInEmail: String,
    signInPassword: String,
    signUpName: String,
    signUpEmail: String,
    signUpPassword: String,
    signUpConfirmPassword: String,
    isTermsAccepted: Boolean,
    signInEmailError: String? = null,
    signUpEmailError: String? = null,
    signInPasswordError: String? = null,
    signUpPasswordError: String? = null,
    nameError: String? = null,
    confirmPasswordError: String? = null,
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
                            containerColor = if (!isSignUpMode) Color.White else Color.Transparent
                        )
                    ) {
                        Text(
                            "ENTRAR",
                            fontWeight = FontWeight.Bold,
                            color = if (!isSignUpMode) DarkNavyBlue else DarkGray
                        )
                    }
                    Button(
                        onClick = { onModeChange(true) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSignUpMode) Color.White else Color.Transparent
                        )
                    ) {
                        Text(
                            "CADASTRAR",
                            fontWeight = FontWeight.Bold,
                            color = if (isSignUpMode) DarkNavyBlue else DarkGray
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
                if (isSignUpMode) {
                    SignUpContent(
                        name = signUpName,
                        onNameChange = onSignUpNameChange,
                        nameError = nameError,
                        email = signUpEmail,
                        onEmailChange = onSignUpEmailChange,
                        emailError = signUpEmailError,
                        password = signUpPassword,
                        onPasswordChange = onSignUpPasswordChange,
                        passwordError = signUpPasswordError,
                        confirmPassword = signUpConfirmPassword,
                        onConfirmPasswordChange = onSignUpConfirmPasswordChange,
                        confirmPasswordError = confirmPasswordError,
                        isTermsAccepted = isTermsAccepted,
                        onTermsAcceptedChange = onTermsAcceptedChange,
                        onSignUpClick = onSignUpClick,
                        onTermsClick = onNavigateToTerms
                    )
                } else {
                    SignInContent(
                        email = signInEmail,
                        onEmailChange = onSignInEmailChange,
                        emailError = signInEmailError,
                        password = signInPassword,
                        onPasswordChange = onSignInPasswordChange,
                        passwordError = signInPasswordError,
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
            isSignUpMode = state.isSignUpMode,
            signInEmail = state.signInEmail,
            signInPassword = state.signInPassword,
            signUpName = state.signUpName,
            signUpEmail = state.signUpEmail,
            signUpPassword = state.signUpPassword,
            signUpConfirmPassword = state.signUpConfirmPassword,
            isTermsAccepted = state.isTermsAccepted,
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
