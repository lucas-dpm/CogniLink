package com.lucasdpm.cognilink.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.ui.components.flashcard.AnswerEditor
import com.lucasdpm.cognilink.ui.components.flashcard.DifficultySelector
import com.lucasdpm.cognilink.ui.components.flashcard.HintEditor
import com.lucasdpm.cognilink.ui.components.flashcard.ShimmerTypeItem
import com.lucasdpm.cognilink.ui.components.flashcard.TrueFalseToggle
import com.lucasdpm.cognilink.ui.components.flashcard.TypeSelector
import com.lucasdpm.cognilink.ui.components.input.CustomTextField
import com.lucasdpm.cognilink.ui.components.utils.CustomSnackbar
import com.lucasdpm.cognilink.ui.components.utils.FullScreenLoading
import com.lucasdpm.cognilink.ui.components.utils.NavigationHeader
import com.lucasdpm.cognilink.ui.components.utils.buttons.DeleteButton
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.components.utils.dialogs.BasicCustomAlertDialog
import com.lucasdpm.cognilink.ui.components.utils.labels.CustomLabel
import com.lucasdpm.cognilink.ui.states.AnswerVisualState
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.Green
import com.lucasdpm.cognilink.ui.theme.Red
import com.lucasdpm.cognilink.ui.theme.shimmerEffect
import com.lucasdpm.cognilink.ui.viewmodels.FlashcardFormViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FlashcardEditorScreen(
    viewModel: FlashcardFormViewModel = koinViewModel(),
    flashcardId: String? = null,
    deckId: String,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(flashcardId, deckId) {
        viewModel.initialize(deckId, flashcardId)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            scope.launch {
                delay(450)
                onNavigateBack()
            }
        }
    }

    if (uiState.showCriticalErrorDialog) {
        BasicCustomAlertDialog(
            onDismissRequest = {
                scope.launch {
                    delay(150)
                    onNavigateBack()
                }
            },
            onConfirmation = {
                scope.launch {
                    delay(150)
                    onNavigateBack()
                }
            },
            dialogTitle = "Ocorreu um erro!",
            dialogText = uiState.errorMessage
                ?: "Não foi possível completar a operação. Por favor, tente novamente mais tarde.",
            confirmationButtonText = "Voltar",
            dismissButtonText = null,
            icon = R.drawable.ic_warning,
            iconColor = Red
        )
    }

    BackHandler {
        if (uiState.wasEdited) {
            viewModel.toggleChangeDialog()
        } else {
            scope.launch {
                delay(150)
                onNavigateBack()
            }
        }
    }

    if (uiState.showDeleteDialog) {
        BasicCustomAlertDialog(
            dialogTitle = "Tem certeza disso?",
            dialogText = "Essa ação não poderá ser desfeita!",
            confirmationButtonText = "Sim",
            dismissButtonText = "Cancelar",
            onConfirmation = { viewModel.deleteFlashcard() },
            onDismissRequest = { viewModel.toggleDeleteDialog() },
        )
    }

    if (uiState.showChangeDialog) {
        BasicCustomAlertDialog(
            onDismissRequest = { viewModel.toggleChangeDialog() },
            onConfirmation = {
                viewModel.discardFlashcard()
                viewModel.toggleChangeDialog()
                scope.launch {
                    delay(150)
                    onNavigateBack()
                }
            },
            dialogTitle = "Alterações não salvas",
            dialogText = "Você possui alterações não salvas. Deseja realmente sair e descartá-las?",
            confirmationButtonText = "Sair e descartar",
            dismissButtonText = "Continuar editando",
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FlashcardEditorContent(
            questionText = uiState.questionText,
            questionTextError = uiState.questionTextError,
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
            isEditMode = uiState.isEditMode,
            onSaveChanges = { viewModel.saveFlashcard() },
            onDeleteClick = { viewModel.toggleDeleteDialog() },
            isMenuExpanded = uiState.isMenuExpanded,
            onMenuClick = viewModel::toggleMenu,
            onBackClick = {
                if (uiState.wasEdited) {
                    viewModel.toggleChangeDialog()
                } else {
                    scope.launch {
                        delay(150)
                        onNavigateBack()
                    }
                }
            },
            snackbarHostState = snackbarHostState,
            isLoading = uiState.isLoading,
        )

        if (uiState.isSaving) {
            FullScreenLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardEditorContent(
    questionText: String = "",
    questionTextError: String? = null,
    onQuestionTextChange: (String) -> Unit = {},
    answerOptions: List<Answer> = emptyList(),
    onSelectCorrectAnswer: (Int) -> Unit = {},
    onBasicAnswerChange: (String) -> Unit = {},
    updateAnswers: (List<Answer>) -> Unit = {},
    onRemoveAnswer: (Answer) -> Unit = {},
    onToggleTrueFalse: (Int) -> Unit = {},
    isRemoveModeActive: Boolean = false,
    onToggleRemoveMode: () -> Unit = {},
    difficulty: DifficultyLevel = DifficultyLevel.EASY,
    onDifficultyChange: (DifficultyLevel) -> Unit = {},
    flashcardType: FlashcardType = FlashcardType.BASIC,
    onTypeChange: (FlashcardType) -> Unit = {},
    hintList: List<String> = emptyList(),
    onHintsUpdate: (List<String>) -> Unit = {},
    onSaveChanges: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    isMenuExpanded: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    isLoading: Boolean = false,
    isEditMode: Boolean = false
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data)
            }
        },
        topBar = {
            NavigationHeader(
                title = if (isEditMode) "EDITAR FLASHCARD"
                else "CRIAR FLASHCARD",
                onBackClick = onBackClick,
                onMenuClick = onMenuClick,
                menuEnabled = isEditMode,
                showMenu = isMenuExpanded,
                menuContent = {
                    if (isEditMode) {
                        DropdownMenuItem(
                            text = { Text("Excluir") },
                            onClick = {
                                onDeleteClick()
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                SimpleGradientButton(
                    text = if (isEditMode) "SALVAR" else "CRIAR",
                    modifier = Modifier.height(40.dp).fillMaxWidth(),
                    icon = R.drawable.ic_check,
                    onClickButton = onSaveChanges
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomLabel(text = "Enunciado do flashcard")
                    Crossfade(
                        targetState = isLoading && isEditMode,
                        label = "flashcard_form_shimmer"
                    ) { loading ->
                        if (loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .shimmerEffect()
                            )
                        } else {
                            CustomTextField(
                                inputValue = questionText,
                                onInputValueChange = onQuestionTextChange,
                                placeholder = "Ex: Calcule o valor de x na equação: 2x + 5 = 15",
                                keyboardType = KeyboardType.Text,
                                errorMessage = questionTextError
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomLabel(text = "Dificuldade")
                    Crossfade(
                        targetState = isLoading && isEditMode,
                        label = "flashcard_form_shimmer"
                    ) { loading ->
                        if (loading) {
                            Box(
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .width(170.dp)
                                    .height(52.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .shimmerEffect()
                            )
                        } else {
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
                    }

                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomLabel(text = "Tipo de Flashcard")
                    Crossfade(
                        targetState = isLoading && isEditMode,
                        label = "flashcard_form_shimmer"
                    ) { loading ->
                        if (loading) {
                            ShimmerTypeItem()
                        }
                        else{
                            TypeSelector(
                                options = FlashcardType.entries,
                                selectedOption = flashcardType,
                                onOptionSelected = onTypeChange
                            )
                        }
                    }

                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CustomLabel(text = "Resposta")
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
                    Crossfade(
                        targetState = isLoading && isEditMode,
                        label = "flashcard_form_shimmer"
                    ) { loading ->
                        if (loading) {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .shimmerEffect()
                                )
                                Box(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .shimmerEffect()
                                )
                            }
                        } else {
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
                                                DeleteButton(onClick = { onRemoveAnswer(answer) })
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
                                                DeleteButton(onClick = { onRemoveAnswer(answer) })
                                            } else {
                                                RadioButton(
                                                    selected = answer.isCorrect,
                                                    onClick = { onSelectCorrectAnswer(index) },
                                                    colors = RadioButtonDefaults.colors(
                                                        selectedColor = Green
                                                    ),
                                                )
                                            }
                                        },
                                        getVisualState = { answer ->
                                            if (answer.isCorrect) AnswerVisualState.Correct
                                            else AnswerVisualState.Incorrect
                                        }
                                    )
                                }

                                FlashcardType.CHAT_FEYNMAN -> {

                                    // Implementação para FlashcardType.CHAT_FEYNMAN

                                }
                            }
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomLabel(text = "Dicas")
                    Crossfade(
                        targetState = isLoading && isEditMode,
                        label = "flashcard_hints_shimmer"
                    ) { loading ->
                        if (loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .shimmerEffect()
                            )
                        } else {
                            HintEditor(
                                hints = hintList,
                                onHintsUpdate = onHintsUpdate
                            )
                        }
                    }
                }
            }

        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FlashcardEditorContentPreview() {
    CogniLinkTheme {
        FlashcardEditorContent(
            questionText = "Question",
            answerOptions = emptyList(),
            difficulty = DifficultyLevel.EASY,
            flashcardType = FlashcardType.MULTIPLE_CHOICE,
            hintList = emptyList(),
            isRemoveModeActive = false,
            isEditMode = true,
        )
    }
}