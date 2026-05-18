package com.example.cognilink.ui.feature.flashcard

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.domain.Answer
import com.example.cognilink.domain.DifficultyLevel
import com.example.cognilink.domain.Flashcard
import com.example.cognilink.domain.FlashcardType
import com.example.cognilink.domain.flashcard1
import com.example.cognilink.ui.components.flashcard.AnswerEditor
import com.example.cognilink.ui.components.flashcard.AnswerVisualState
import com.example.cognilink.ui.components.flashcard.DifficultySelector
import com.example.cognilink.ui.components.flashcard.HintEditor
import com.example.cognilink.ui.components.flashcard.TrueFalseToggle
import com.example.cognilink.ui.components.flashcard.TypeSelector
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.buttons.DeleteButton
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.components.utils.labels.CustomLabel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Green
import com.example.cognilink.viewmodel.FlashcardEditorUiState
import com.example.cognilink.viewmodel.FlashcardEditorViewModel

@Composable
fun EditorScreen(
    viewModel: FlashcardEditorViewModel = viewModel(),
    flashcard: Flashcard? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(flashcard) {
        if (flashcard != null) {
            viewModel.loadFlashcard(flashcard)
        }
    }
    EditorContent(
        questionText = uiState.questionText,
        onQuestionTextChange = viewModel::onQuestionTextChange,
        answerOptions = uiState.answerOptions,
        updateAnswers = viewModel::updateAnswers,
        onRemoveAnswer = viewModel::removeAnswer,
        onToggleTrueFalse = viewModel::toggleTrueFalseAnswer,
        onSelectCorrectAnswer = viewModel::selectCorrectAnswer,
        onBasicAnswerChange = viewModel::onBasicAnswerChange,
        difficulty = uiState.difficulty,
        onDifficultyChange = viewModel::onDifficultyChange,
        flashcardType = uiState.cardType,
        onTypeChange = viewModel::onTypeChange,
        hintList = uiState.hints,
        onHintsUpdate = viewModel::updateHints,
        isRemoveModeActive = uiState.isDeleteMode,
        onToggleRemoveMode = viewModel::toggleDeletionMode,
        isEditMode = flashcard != null,
        onSaveChanges = viewModel::saveFlashcard
    )
}

@Composable
fun EditorContent(
    modifier: Modifier = Modifier,
    questionText: String = "",
    onQuestionTextChange: (String) -> Unit = {},
    answerOptions: List<Answer> = emptyList(),
    updateAnswers: (List<Answer>) -> Unit,
    onRemoveAnswer: (Answer) -> Unit,
    onToggleTrueFalse: (Int) -> Unit,
    onSelectCorrectAnswer: (Int) -> Unit,
    onBasicAnswerChange: (String) -> Unit = {},
    difficulty: DifficultyLevel = DifficultyLevel.EASY,
    onDifficultyChange: (DifficultyLevel) -> Unit = {},
    flashcardType: FlashcardType = FlashcardType.BASIC,
    onTypeChange: (FlashcardType) -> Unit = {},
    hintList: List<String> = emptyList(),
    onHintsUpdate: (List<String>) -> Unit = {},
    isEditMode: Boolean = false,
    isRemoveModeActive: Boolean = false,
    onToggleRemoveMode: () -> Unit = {},
    onSaveChanges: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            val paddingValue = 24.dp
            Column(modifier = Modifier.padding(paddingValue)) {
                SimpleGradientButton(
                    text = if(isEditMode) "SALVAR" else "CRIAR",
                    height = 40.dp,
                    icon = R.drawable.ic_check,
                    onClickButton = onSaveChanges
                )
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            NavigationHeader(
                title = if (isEditMode) "EDITAR FLASHCARD"
                else "CRIAR FLASHCARD"
            )
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                CustomTextField(
                    inputValue = questionText,
                    onInputValueChange = onQuestionTextChange,
                    label = {
                        CustomLabel(
                            text = "Enunciado do flashcard",
                            textColor = DarkGray
                        )
                    },
                    placeholder = "Ex: Calcule o valor de x na equação: 2x + 5 = 15",
                    keyboardType = KeyboardType.Text
                )

                Column {
                    CustomLabel("Dificuldade")
                    DifficultySelector(
                        difficultyLevels = DifficultyLevel.entries,
                        selectedDifficulty = difficulty,
                        onDifficultySelected = { newDifficulty ->
                            if (newDifficulty != null) {
                                onDifficultyChange(newDifficulty)
                            }
                        },
                        modifier = Modifier.width(150.dp)
                    )
                }

                Column {
                    CustomLabel("Tipo de Flashcard")
                    TypeSelector(
                        options = FlashcardType.entries,
                        selectedOption = flashcardType,
                        onOptionSelected = onTypeChange
                    )
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CustomLabel("Resposta")
                        if (flashcardType == FlashcardType.MULTIPLE_CHOICE || flashcardType == FlashcardType.TRUE_OR_FALSE)
                            Text(
                                text = if (isRemoveModeActive) "VOLTAR PARA SELEÇÃO" else "GERENCIAR",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = DarkNavyBlue,
                                modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { onToggleRemoveMode() }
                            )
                    }
                    when (flashcardType) {
                        FlashcardType.BASIC -> {
                            CustomTextField(
                                inputValue = answerOptions.firstOrNull()?.answer ?: "",
                                onInputValueChange = onBasicAnswerChange,
                                placeholder = "Ex: Paris"
                            )
                        }

                        FlashcardType.TRUE_OR_FALSE -> {
                            AnswerEditor(
                                answerOptions = answerOptions,
                                onAnswerOptionsUpdate = updateAnswers,
                                selectionControl = { answer, index ->
                                    if (isRemoveModeActive) {
                                        DeleteButton( onClick = { onRemoveAnswer(answer) } )
                                    } else
                                        TrueFalseToggle(
                                            currentValue = if (answer.isCorrect) "T" else "F",
                                            onToggle = { onToggleTrueFalse(index) }
                                        )
                                },
                                getVisualState = { answer ->
                                    if (answer.isCorrect) AnswerVisualState.Correct
                                    else AnswerVisualState.Incorrect
                                },
                                limit = 10
                            )
                        }

                        FlashcardType.MULTIPLE_CHOICE -> {
                            AnswerEditor(
                                answerOptions = answerOptions,
                                onAnswerOptionsUpdate = updateAnswers,
                                selectionControl = { answer, index ->
                                    if (isRemoveModeActive) {
                                        DeleteButton( onClick = { onRemoveAnswer(answer) } )
                                    } else {
                                        RadioButton(
                                            selected = answer.isCorrect,
                                            onClick = { onSelectCorrectAnswer(index) },
                                            colors = RadioButtonDefaults.colors(selectedColor = Green),
                                        )
                                    }
                                },
                                getVisualState = { answer ->
                                    if (answer.isCorrect) AnswerVisualState.Correct
                                    else AnswerVisualState.Incorrect
                                }
                            )
                        }

                        FlashcardType.OMISSION -> {

                            // Implementação para FlashcardType.OMISSION

                        }

                        FlashcardType.CHAT_FEYNMAN -> {

                            // Implementação para FlashcardType.CHAT_FEYNMAN

                        }
                    }
                }

                Column {
                    CustomLabel("Dicas")
                    HintEditor(
                        hints = hintList,
                        onHintsUpdate = onHintsUpdate
                    )
                }
            }

        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EditorContentPreview() {
    CogniLinkTheme {
        EditorContent(
            questionText = flashcard1.question,
            onQuestionTextChange = {},
            answerOptions = flashcard1.answerOptions,
            updateAnswers = {},
            onRemoveAnswer = {},
            onToggleTrueFalse = {},
            onSelectCorrectAnswer = {},
            onBasicAnswerChange = {},
            difficulty = flashcard1.difficulty,
            onDifficultyChange = {},
            flashcardType = flashcard1.cardType,
            onTypeChange = {},
            hintList = flashcard1.hints,
            onHintsUpdate = {},
            isRemoveModeActive = false,
            onToggleRemoveMode = {},
            isEditMode = true,
            onSaveChanges = {}
        )
    }
}
