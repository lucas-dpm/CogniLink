package com.lucasdpm.cognilink.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.ui.components.utils.NavigationHeader
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.viewmodels.TermsViewModel
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.koin.androidx.compose.koinViewModel

@Composable
fun TermsScreen(
    onNavigateBack: () -> Unit,
    viewModel: TermsViewModel = koinViewModel()
) {
    TermsContent(
        onNavigateBack = onNavigateBack,
        termsMarkdown = viewModel.termsText.trimIndent()
    )
}

@Composable
fun TermsContent(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    termsMarkdown: String,
) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = modifier
            .imePadding()
            .statusBarsPadding(),
        containerColor = OffWhite,
        topBar = {
            NavigationHeader(
                title = "Termos de Uso e Política de Privacidade",
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .verticalScroll(scrollState)) {
            MarkdownText(
                markdown = termsMarkdown,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun TermsContentPreview() {
    TermsContent(
        onNavigateBack = {},
        termsMarkdown = """
            # Termos de Uso
            
            Bem-vindo ao CogniLink. Ao usar nosso aplicativo, você concorda com nossos termos...
        """.trimIndent()
    )
}
