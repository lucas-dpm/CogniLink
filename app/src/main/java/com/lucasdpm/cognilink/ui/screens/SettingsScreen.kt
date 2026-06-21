package com.lucasdpm.cognilink.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.data.model.StudyContext
import com.lucasdpm.cognilink.ui.components.settings.ContextDropdown
import com.lucasdpm.cognilink.ui.components.settings.SettingItem
import com.lucasdpm.cognilink.ui.components.utils.NavigationHeader
import com.lucasdpm.cognilink.ui.states.SettingsUiState
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.theme.Red
import com.lucasdpm.cognilink.ui.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    userId: String,
    onNavigateBack: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToEditContext: (String) -> Unit,
    onNavigateToCreateContext: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToTerms: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadContexts(userId)
    }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onNavigateToLogin()
        }
    }

    SettingsScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToChangePassword = onNavigateToChangePassword,
        onNavigateToEditContext = onNavigateToEditContext,
        onNavigateToCreateContext = { onNavigateToCreateContext(userId) },
        onLogout = { viewModel.logout() },
        onDeleteContext = viewModel::deleteContext,
        onNavigateToTerms = onNavigateToTerms
    )
}

@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState,
    onNavigateBack: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToEditContext: (String) -> Unit,
    onNavigateToCreateContext: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onLogout: () -> Unit,
    onDeleteContext: (StudyContext) -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            NavigationHeader(
                title = "Configurações",
                onBackClick = onNavigateBack
            )
        },
        containerColor = OffWhite,
        modifier = Modifier.statusBarsPadding()
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Conta",
                    color = DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                SettingItem(
                    title = "Alterar Senha",
                    icon = R.drawable.ic_lock,
                    onClick = onNavigateToChangePassword
                )

                SettingItem(
                    title = "Sair",
                    icon = R.drawable.ic_logout,
                    onClick = onLogout,
                    iconColor = Red
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Estudo",
                    color = DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                ContextDropdown(
                    contexts = uiState.contexts,
                    onEdit = onNavigateToEditContext,
                    onDelete = onDeleteContext,
                    onAdd = onNavigateToCreateContext
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Sobre",
                    color = DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                SettingItem(
                    title = "Termos e Serviços",
                    icon = R.drawable.ic_info,
                    onClick = onNavigateToTerms,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val sampleContexts = listOf(
        StudyContext(
            id = "1",
            userId = "user1",
            name = "Casa",
            latitude = -23.5505,
            longitude = -46.6333,
            radius = 100f,
            dwellTimeMillis = 300000
        ),
        StudyContext(
            id = "2",
            userId = "user1",
            name = "Trabalho",
            latitude = -23.5596,
            longitude = -46.7201,
            radius = 500f,
            dwellTimeMillis = 1800000
        )
    )

    val uiState = SettingsUiState(
        contexts = sampleContexts,
        isLoading = false,
        isLoggedOut = false
    )

    CogniLinkTheme {
        SettingsScreenContent(
            uiState = uiState,
            onNavigateBack = {},
            onNavigateToChangePassword = {},
            onNavigateToEditContext = {},
            onNavigateToCreateContext = {},
            onNavigateToTerms = {},
            onLogout = {},
            onDeleteContext = {}
        )
    }
}

