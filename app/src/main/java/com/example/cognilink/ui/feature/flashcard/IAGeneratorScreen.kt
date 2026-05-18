package com.example.cognilink.ui.feature.flashcard

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.domain.DifficultyLevel
import com.example.cognilink.domain.FlashcardType
import com.example.cognilink.ui.components.flashcard.DifficultySelector
import com.example.cognilink.ui.components.flashcard.QuantitySelector
import com.example.cognilink.ui.components.flashcard.TypeOptionList
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.input.FileUploadArea
import com.example.cognilink.ui.components.utils.labels.LabeledText
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.buttons.NeonActionButton
import com.example.cognilink.ui.components.utils.labels.CustomLabel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.viewmodel.IAGeneratorViewModel
import kotlin.enums.enumEntries

@Composable
fun IAGeneratorScreen(
    modifier: Modifier = Modifier,
    viewModel: IAGeneratorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.selectedType?.let { selectedType ->
        IAGeneratorContent(
            modifier = modifier,
            flashcardTheme = uiState.flashcardTheme,
            onFlashcardThemeChange = viewModel::onThemeChange,
            quantity = uiState.quantity,
            onQuantityChange = viewModel::onQuantityChange,
            selectedDifficulty = uiState.selectedDifficulty,
            onDifficultyChange = viewModel::onDifficultyChange,
            typeOptions = uiState.typeOptions,
            selectedType = selectedType,
            onTypeChange = viewModel::onTypeChange,
            onGenerateClick = viewModel::generateFlashcards,
            isLoading = uiState.isLoading
        )
    }
}

@Composable
fun IAGeneratorContent(
    modifier: Modifier = Modifier,
    flashcardTheme: String = "",
    onFlashcardThemeChange: (String) -> Unit = {},
    quantity: Int = 1,
    onQuantityChange: (Int) -> Unit = {},
    selectedDifficulty: DifficultyLevel? = null,
    onDifficultyChange: (DifficultyLevel?) -> Unit = {},
    typeOptions: List<FlashcardType> = emptyList(),
    selectedType: FlashcardType? = null,
    onTypeChange: (FlashcardType?) -> Unit = {},
    onGenerateClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .statusBarsPadding(),
        topBar = { NavigationHeader(title = "CRIAR NOVO FLASHCARD") },
        containerColor = OffWhite
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // --- SEÇÃO TEMA ---
            Column {
                CustomTextField(
                    inputValue = flashcardTheme,
                    onInputValueChange = onFlashcardThemeChange,
                    label = {
                        CustomLabel(
                            text = "Tema do flashcard",
                            textColor = DarkGray
                        )
                    },
                    placeholder = "Ex: Mitocôndrias e Ciclo de Krebs",
                    keyboardType = KeyboardType.Text
                )
                LabeledText(
                    modifier = Modifier.padding(top = 8.dp),
                    label = "OBS: ",
                    text = "Se nulo, a IA criará com base no arquivo anexo. O tema será preenchido após a criação."
                )
            }

            // --- SEÇÃO ANEXO ---
            Column {
                CustomLabel(text = "Anexo de Arquivo (Opcional)")

                FileUploadArea(onUploadClick = { /* TODO */ })

                LabeledText(
                    modifier = Modifier.padding(top = 8.dp),
                    label = "OBS: ",
                    text = "Se nulo, a IA criará com base no tema. Pelo menos o TEMA ou um ARQUIVO deve ser fornecido."
                )
            }

            // --- SEÇÃO SELETOR ---

            TypeOptionList(
                options = typeOptions,
                selectedOption = selectedType,
                onOptionSelected = onTypeChange
            )


            // --- SEÇÃO CONFIGURAÇÕES (Dificuldade e Quantidade) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    CustomLabel("Dificuldade")
                    DifficultySelector(
                        difficultyLevels = DifficultyLevel.entries,
                        selectedDifficulty = selectedDifficulty,
                        onDifficultySelected = onDifficultyChange,
                        includeAllOption = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    CustomLabel("Quantidade")
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
                onClickButton = onGenerateClick
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
            onQuantityChange = {},
            selectedDifficulty = DifficultyLevel.MEDIUM,
            onDifficultyChange = {},
            typeOptions = FlashcardType.entries,
            selectedType = FlashcardType.MULTIPLE_CHOICE,
            onTypeChange = {},
            onGenerateClick = {},
            isLoading = false
        )
    }
}
