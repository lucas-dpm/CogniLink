package com.lucasdpm.cognilink.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.data.model.Flashcard
import com.lucasdpm.cognilink.data.preview.PreviewDataProvider
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.ui.components.flashcard.AIFeedbackSection
import com.lucasdpm.cognilink.ui.components.flashcard.AnswerSelector
import com.lucasdpm.cognilink.ui.components.flashcard.FlashcardHeader
import com.lucasdpm.cognilink.ui.components.flashcard.HintReveal
import com.lucasdpm.cognilink.ui.components.flashcard.TrueFalseToggle
import com.lucasdpm.cognilink.ui.components.input.CustomTextField
import com.lucasdpm.cognilink.ui.components.utils.CustomSnackbar
import com.lucasdpm.cognilink.ui.components.utils.FullScreenLoading
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.components.utils.dialogs.BasicCustomAlertDialog
import com.lucasdpm.cognilink.ui.states.AnswerVisualState
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.Green
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.theme.Red
import com.lucasdpm.cognilink.ui.theme.VeryLightGray
import com.lucasdpm.cognilink.ui.theme.White
import com.lucasdpm.cognilink.ui.viewmodels.StudySessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun StudySessionScreen(
    studyMode: String,
    contextId: String,
    onNavigateBack: () -> Unit,
    viewModel: StudySessionViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(studyMode, contextId) {
        viewModel.initializeSession(studyMode, contextId)
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
            dialogText = uiState.errorMessage ?: "Não foi possível iniciar a sessão.",
            confirmationButtonText = "Voltar",
            dismissButtonText = null,
            icon = R.drawable.ic_warning,
            iconColor = Red
        )
    }

    LaunchedEffect(uiState.isSessionFinished) {
        if (uiState.isSessionFinished && !uiState.isSessionInsightDialogOpen) {
            viewModel.toggleSessionInsightDialog()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        uiState.currentFlashcard?.let { flashcard ->
            StudySessionContent(
                flashcard = flashcard,
                currentFlashcardIndex = uiState.currentFlashcardIndex,
                totalFlashcards = uiState.sessionFlashcards.size,
                sessionTitle = uiState.sessionTitle,
                selectedAnswers = uiState.selectedAnswers,
                onSelectAnswer = viewModel::onSelectAnswer,
                isQuestionAnswered = uiState.isQuestionAnswered,
                isQuestionVerified = uiState.isQuestionVerified,
                isCloseDialogOpen = uiState.isCloseDialogOpen,
                isSessionInsightDialogOpen = uiState.isSessionInsightDialogOpen,
                isLastFlashcard = uiState.isLastFlashcard,
                elapsedTime = viewModel.formatSeconds(uiState.secondsElapsed),
                isAnswerCorrect = uiState.isAnswerCorrect,
                aiFeedback = uiState.aiFeedback,
                sequenceHits = uiState.sequenceHits,
                isValidating = uiState.isValidating,
                onDismissSessionInsight = {
                    viewModel.toggleSessionInsightDialog()
                    scope.launch {
                        delay(100)
                        onNavigateBack()
                    }
                },
                onCloseClick = viewModel::toggleCloseDialog,
                onAcceptCloseDialog = {
                    viewModel.toggleCloseDialog()
                    scope.launch {
                        delay(120)
                        onNavigateBack()
                    }
                },
                onDismissCloseDialog = {
                    scope.launch {
                        delay(120)
                        viewModel.toggleCloseDialog()
                    }
                },
                onClickToVerifyQuestion = viewModel::verifyQuestion,
                onClickToNextFlashcard = viewModel::nextFlashcard,
            )
        }

        if (uiState.isLoading) {
            FullScreenLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySessionContent(
    modifier: Modifier = Modifier,
    flashcard: Flashcard,
    currentFlashcardIndex: Int,
    totalFlashcards: Int,
    sessionTitle: String = "",
    selectedAnswers: Map<Answer, String> = mapOf(),
    onSelectAnswer: (Answer, String) -> Unit = { _, _ -> },
    isQuestionAnswered: Boolean,
    isQuestionVerified: Boolean,
    isCloseDialogOpen: Boolean = false,
    isSessionInsightDialogOpen: Boolean = false,
    isLastFlashcard: Boolean = false,
    elapsedTime: String,
    isAnswerCorrect: Boolean = false,
    aiFeedback: String? = null,
    sequenceHits: Int = 0,
    isValidating: Boolean = false,
    onDismissSessionInsight: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    onAcceptCloseDialog: () -> Unit = {},
    onDismissCloseDialog: () -> Unit = {},
    onClickToVerifyQuestion: () -> Unit = {},
    onClickToNextFlashcard: () -> Unit = {},
) {

    val scrollState = rememberScrollState()
    val isKeyboardVisible = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp

    if (isCloseDialogOpen) {
        BasicCustomAlertDialog(
            onDismissRequest = onDismissCloseDialog,
            onConfirmation = onAcceptCloseDialog,
            dialogTitle = "Tem certeza disso?",
            dialogText = "O progresso não será salvo! Deseja realmente sair?",
            confirmationButtonText = "Sair",
            dismissButtonText = "Cancelar",
            icon = R.drawable.ic_warning,
            iconColor = Red
        )
    }

    if (isSessionInsightDialogOpen) {
        BasicAlertDialog(
            onDismissRequest = onDismissSessionInsight,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(28.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sessão Finalizada!",
                        fontWeight = FontWeight.Bold,
                        color = DarkNavyBlue,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Você completou todos os flashcards desta sessão.",
                        color = DarkGray,
                        textAlign = TextAlign.Center
                    )
                    SimpleGradientButton(
                        text = "Voltar ao Início",
                        onClickButton = onDismissSessionInsight
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            FlashcardHeader(
                title = sessionTitle, onCloseClick = onCloseClick,
                actualCard = currentFlashcardIndex + 1,
                totalCards = totalFlashcards
            )
        },
        bottomBar = {
            if (!isKeyboardVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding()
                ) {
                    SimpleGradientButton(
                        text = when {
                            isValidating -> "VALIDANDO..."
                            isQuestionVerified && isLastFlashcard -> "FINALIZAR SESSÃO"
                            isQuestionVerified -> "PROXÍMO FLASHCARD"
                            else -> "VERIFICAR RESPOSTA"
                        },
                        icon = if (isValidating) null else if (isQuestionVerified) R.drawable.ic_arrow_forward else R.drawable.ic_check,
                        iconRightSide = true,
                        isEnabled = isQuestionAnswered && !isValidating,
                        onClickButton = {
                            if (isQuestionVerified) {
                                onClickToNextFlashcard()
                            } else {
                                onClickToVerifyQuestion()
                            }
                        }
                    )
                }
            }
        },
        containerColor = OffWhite,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    color = VeryLightGray.copy(alpha = 0.5f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            tint = DarkNavyBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "TEMPO: $elapsedTime",
                            fontWeight = FontWeight.SemiBold,
                            color = DarkGray,
                            fontSize = 12.sp
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    color = VeryLightGray.copy(alpha = 0.5f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cached),
                            contentDescription = null,
                            tint = DarkNavyBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "SEQUÊNCIA: $sequenceHits",
                            fontWeight = FontWeight.SemiBold,
                            color = DarkGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            AnimatedContent(
                targetState = flashcard,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                }
            ) { targetFlashcard ->
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Surface(
                        shape = RoundedCornerShape(32.dp),
                        shadowElevation = 2.dp,
                        color = White
                    ) {
                        Text(
                            text = targetFlashcard.question,
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkNavyBlue,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(32.dp)
                                .fillMaxWidth()
                        )
                    }

                    when (targetFlashcard.cardType) {
                        FlashcardType.BASIC -> {

                            CustomTextField(
                                inputValue = selectedAnswers.values.firstOrNull() ?: "",
                                onInputValueChange = { newAnswer ->
                                    onSelectAnswer(Answer("", false), newAnswer)
                                },
                                placeholder = "Sua resposta",
                                minLines = 3,
                                enabled = !isQuestionVerified
                            )

                            if (isQuestionVerified) {
                                Surface(
                                    color = White,
                                    shape = RoundedCornerShape(24.dp),
                                    shadowElevation = 2.dp,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                painterResource(
                                                    id = if (isAnswerCorrect) {
                                                        R.drawable.ic_check_circle
                                                    } else {
                                                        R.drawable.ic_cancel
                                                    }
                                                ), contentDescription = null,
                                                tint = if (isAnswerCorrect) {
                                                    Green
                                                } else {
                                                    Red
                                                },
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = "Resposta " + if (isAnswerCorrect) {
                                                    "Correta"
                                                } else {
                                                    "Incorreta"
                                                },
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = DarkNavyBlue,
                                                lineHeight = 8.sp
                                            )
                                        }
                                        Text(
                                            text = flashcard.answerOptions.firstOrNull()?.answer
                                                ?: "",
                                            color = DarkGray,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 14.sp,
                                            lineHeight = 16.sp
                                        )
                                        AIFeedbackSection(
                                            aiFeedback = aiFeedback,
                                        )
                                    }
                                }
                            }
                        }

                        FlashcardType.TRUE_OR_FALSE -> {
                            AnswerSelector(
                                answerOptions = targetFlashcard.answerOptions,
                                getVisualState = { answer ->
                                    val userChoice = selectedAnswers[answer] // "T" ou "F" ou null
                                    val isAnswerCorrectType = answer.isCorrect

                                    if (isQuestionVerified) {
                                        when {
                                            (userChoice == "T" && isAnswerCorrectType) || (userChoice == "F" && !isAnswerCorrectType) -> {
                                                AnswerVisualState.Correct
                                            }

                                            userChoice != null -> AnswerVisualState.Incorrect
                                            else -> AnswerVisualState.Default
                                        }
                                    } else {
                                        if (userChoice != null) AnswerVisualState.Selected else AnswerVisualState.Default
                                    }
                                },
                                selectionControl = { answer, _ ->
                                    if (!isQuestionVerified) {
                                        TrueFalseToggle(
                                            currentValue = selectedAnswers[answer],
                                            onToggle = { choice ->
                                                onSelectAnswer(answer, choice)
                                            },
                                            enabled = true
                                        )
                                    }
                                },
                            )
                        }

                        FlashcardType.MULTIPLE_CHOICE -> {
                            AnswerSelector(
                                answerOptions = targetFlashcard.answerOptions,
                                getVisualState = { answer ->
                                    val isSelected = selectedAnswers.contains(answer)
                                    if (isQuestionVerified) {
                                        if (answer.isCorrect) AnswerVisualState.Correct
                                        else if (isSelected) AnswerVisualState.Incorrect
                                        else AnswerVisualState.Default
                                    } else {
                                        if (isSelected) AnswerVisualState.Selected else AnswerVisualState.Default
                                    }
                                },
                                selectionControl = { answer, _ ->
                                    if (!isQuestionVerified) {
                                        RadioButton(
                                            selected = (selectedAnswers.contains(answer)),
                                            onClick = { onSelectAnswer(answer, "") },
                                            enabled = true,
                                            colors = RadioButtonDefaults.colors(selectedColor = DarkNavyBlue),
                                        )
                                    }
                                }
                            )
                            if (isQuestionVerified) {
                                AIFeedbackSection(
                                    aiFeedback = aiFeedback,
                                )
                            }
                        }

                        FlashcardType.OMISSION -> {
                            CustomTextField(
                                inputValue = selectedAnswers.values.firstOrNull() ?: "",
                                onInputValueChange = { newAnswer ->
                                    onSelectAnswer(Answer("", false), newAnswer)
                                },
                                placeholder = "Preencha a lacuna",
                                enabled = !isQuestionVerified
                            )
                            if (isQuestionVerified) {
                                AIFeedbackSection(
                                    aiFeedback = aiFeedback,
                                )
                            }
                        }

                        FlashcardType.CHAT_FEYNMAN -> {

                        }
                    }
                    HintReveal(hints = targetFlashcard.hints)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StudySessionContentPreview() {
    CogniLinkTheme {
        StudySessionContent(
            flashcard = PreviewDataProvider.flashcard,
            currentFlashcardIndex = 0,
            totalFlashcards = 10,
            sessionTitle = "Kotlin",
            selectedAnswers = emptyMap(),
            isQuestionAnswered = false,
            isQuestionVerified = true,
            isCloseDialogOpen = false,
            isAnswerCorrect = true,
            elapsedTime = "00:00",
        )
    }
}
