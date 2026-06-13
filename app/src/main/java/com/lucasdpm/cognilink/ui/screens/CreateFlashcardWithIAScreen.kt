package com.lucasdpm.cognilink.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.ui.components.flashcard.DifficultySelector
import com.lucasdpm.cognilink.ui.components.flashcard.QuantitySelector
import com.lucasdpm.cognilink.ui.components.flashcard.TypeOptionList
import com.lucasdpm.cognilink.ui.components.input.CustomTextField
import com.lucasdpm.cognilink.ui.components.input.FileUploadArea
import com.lucasdpm.cognilink.ui.components.utils.NavigationHeader
import com.lucasdpm.cognilink.ui.components.utils.buttons.NeonActionButton
import com.lucasdpm.cognilink.ui.components.utils.labels.CustomLabel
import com.lucasdpm.cognilink.ui.components.utils.labels.LabeledText
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.viewmodels.IAGeneratorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateFlashcardWithIAScreen(
    deckId: String,
    viewModel: IAGeneratorViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(deckId) {
        viewModel.initialize(deckId)
    }

    IAGeneratorContent(
        flashcardTheme = uiState.flashcardTheme,
        themeError = uiState.themeError,
        onFlashcardThemeChange = viewModel::onThemeChange,
        quantity = uiState.quantity,
        onQuantityChange = viewModel::onQuantityChange,
        selectedDifficulty = uiState.selectedDifficulty,
        onDifficultyChange = viewModel::onDifficultyChange,
        typeOptions = uiState.typeOptions,
        selectedType = uiState.selectedType,
        onTypeChange = viewModel::onTypeChange,
        onGenerateClick = viewModel::generateFlashcards,
        hasFile = uiState.hasFile,
        onUploadClick = viewModel::onUploadFile,
        isLoading = uiState.isLoading,
        onBackClick = onNavigateBack
    )

}

@Composable
fun IAGeneratorContent(
    flashcardTheme: String = "",
    themeError: String? = null,
    quantity: Int = 1,
    selectedDifficulty: DifficultyLevel? = null,
    typeOptions: List<FlashcardType> = emptyList(),
    selectedType: FlashcardType? = null,
    hasFile: Boolean = false,
    isLoading: Boolean = false,
    onQuantityChange: (Int) -> Unit = {},
    onFlashcardThemeChange: (String) -> Unit = {},
    onDifficultyChange: (DifficultyLevel?) -> Unit = {},
    onTypeChange: (FlashcardType?) -> Unit = {},
    onGenerateClick: () -> Unit = {},
    onUploadClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .statusBarsPadding(),
        topBar = { NavigationHeader(title = "CRIAR NOVO FLASHCARD", onBackClick = onBackClick) },
        containerColor = OffWhite
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Column {
                CustomTextField(inputValue = flashcardTheme, onInputValueChange = onFlashcardThemeChange, label = {
                        CustomLabel(
                            text = "Tema do flashcard",
                            textColor = DarkGray
                        )
                    }, placeholder = "Ex: Mitocôndrias e Ciclo de Krebs", keyboardType = KeyboardType.Text, errorMessage = themeError)
                LabeledText(
                    modifier = Modifier.padding(top = 8.dp),
                    label = "OBS: ",
                    text = "Se nulo, a IA criará com base no arquivo anexo. O tema será preenchido após a criação."
                )
            }

            Column {
                CustomLabel(text = "Anexo de Arquivo (Opcional)")

                FileUploadArea(
                    onUploadClick = onUploadClick
                )

                LabeledText(
                    modifier = Modifier.padding(top = 8.dp),
                    label = "OBS: ",
                    text = "Se nulo, a IA criará com base no tema. Pelo menos o TEMA ou um ARQUIVO deve ser fornecido."
                )
            }

            TypeOptionList(
                options = typeOptions,
                selectedOption = selectedType,
                onOptionSelected = onTypeChange
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    CustomLabel(text = "Dificuldade")
                    DifficultySelector(
                        difficultyLevels = DifficultyLevel.entries,
                        selectedDifficulty = selectedDifficulty,
                        onDifficultySelected = onDifficultyChange,
                        includeAllOption = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    CustomLabel(text = "Quantidade")
                    QuantitySelector(
                        quantity = quantity,
                        onQuantityChange = onQuantityChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            NeonActionButton(
                text = if (isLoading) "GERANDO..." else "GERAR FLASHCARDS COM IA",
                icon = R.drawable.ic_stars,
                onClickButton = onGenerateClick,
                isEnabled = !isLoading && (flashcardTheme.isNotBlank() || hasFile),
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun IAGeneratorContentPreview() {
    CogniLinkTheme {
        IAGeneratorContent(
            flashcardTheme = "Mitocôndrias",
            onFlashcardThemeChange = {},
            quantity = 5,
            selectedDifficulty = DifficultyLevel.MEDIUM,
            typeOptions = FlashcardType.entries,
            selectedType = FlashcardType.MULTIPLE_CHOICE,
            hasFile = false,
            isLoading = false,
        )
    }
}
