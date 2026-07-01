package com.lucasdpm.cognilink.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.lucasdpm.cognilink.domain.repository.FeynmanChatMessage
import com.lucasdpm.cognilink.ui.components.flashcard.AIFeedbackSection
import com.lucasdpm.cognilink.ui.components.flashcard.AnswerSelector
import com.lucasdpm.cognilink.ui.components.flashcard.FlashcardHeader
import com.lucasdpm.cognilink.ui.components.flashcard.HintReveal
import com.lucasdpm.cognilink.ui.components.flashcard.TrueFalseToggle
import com.lucasdpm.cognilink.ui.components.input.CustomTextField
import com.lucasdpm.cognilink.ui.components.utils.FullScreenLoading
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.components.utils.dialogs.BasicCustomAlertDialog
import com.lucasdpm.cognilink.ui.states.AnswerVisualState
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.Green
import com.lucasdpm.cognilink.ui.theme.LightNavyBlue
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.theme.Red
import com.lucasdpm.cognilink.ui.theme.VeryLightGray
import com.lucasdpm.cognilink.ui.theme.White
import com.lucasdpm.cognilink.ui.viewmodels.StudySessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySessionScreen(
    studyMode: String,
    contextId: String,
    userId: String,
    onNavigateBack: () -> Unit,
    viewModel: StudySessionViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(studyMode, contextId, userId) {
        viewModel.initializeSession(studyMode, contextId, userId)
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

    if (uiState.isCloseDialogOpen) {
        BasicCustomAlertDialog(
            onDismissRequest = { viewModel.toggleCloseDialog() },
            onConfirmation = {
                viewModel.toggleCloseDialog()
                scope.launch {
                    delay(120)
                    onNavigateBack()
                }
            },
            dialogTitle = "Tem certeza disso?",
            dialogText = "O progresso não será salvo! Deseja realmente sair?",
            confirmationButtonText = "Sair",
            dismissButtonText = "Cancelar",
            icon = R.drawable.ic_warning,
            iconColor = Red
        )
    }

    if (uiState.isSessionInsightDialogOpen) {
        BasicAlertDialog(
            onDismissRequest = {
                viewModel.toggleSessionInsightDialog()
                scope.launch {
                    delay(100)
                    onNavigateBack()
                }
            },
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
                        onClickButton = {
                            viewModel.toggleSessionInsightDialog()
                            scope.launch {
                                delay(100)
                                onNavigateBack()
                            }
                        }
                    )
                }
            }
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
                isLastFlashcard = uiState.isLastFlashcard,
                elapsedTime = viewModel.formatSeconds(uiState.secondsElapsed),
                isAnswerCorrect = uiState.isAnswerCorrect,
                aiFeedback = uiState.aiFeedback,
                sequenceHits = uiState.sequenceHits,
                isValidating = uiState.isValidating,
                feynmanChatMessages = uiState.feynmanChatMessages,
                isFeynmanTyping = uiState.isFeynmanTyping,
                feynmanPersonaName = uiState.feynmanPersonaName,
                feynmanErrorMessage = uiState.feynmanErrorMessage,
                onCloseClick = viewModel::toggleCloseDialog,
                onClickToVerifyQuestion = viewModel::verifyQuestion,
                onClickToNextFlashcard = viewModel::nextFlashcard,
                onSendFeynmanMessage = viewModel::sendFeynmanChatMessage
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
    isLastFlashcard: Boolean = false,
    elapsedTime: String,
    isAnswerCorrect: Boolean = false,
    aiFeedback: String? = null,
    sequenceHits: Int = 0,
    isValidating: Boolean = false,
    feynmanChatMessages: List<FeynmanChatMessage> = emptyList(),
    isFeynmanTyping: Boolean = false,
    feynmanPersonaName: String? = null,
    feynmanErrorMessage: String? = null,
    onCloseClick: () -> Unit = {},
    onClickToVerifyQuestion: () -> Unit = {},
    onClickToNextFlashcard: () -> Unit = {},
    onSendFeynmanMessage: (String) -> Unit = {}
) {

    val scrollState = rememberScrollState()
    val isKeyboardVisible = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp
    var feynmanInputText by remember { mutableStateOf("") }

    LaunchedEffect(feynmanChatMessages.size, isFeynmanTyping) {
        scrollState.animateScrollTo(scrollState.maxValue)
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
            if (!isKeyboardVisible || flashcard.cardType == FlashcardType.CHAT_FEYNMAN) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding()
                ) {
                    if (flashcard.cardType == FlashcardType.CHAT_FEYNMAN && !isQuestionVerified) {
                        Surface(
                            shape = RoundedCornerShape(32.dp),
                            color = White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = CenterVertically
                            ) {
                                CustomTextField(
                                    modifier = Modifier.weight(1f),
                                    inputValue = feynmanInputText,
                                    onInputValueChange = { feynmanInputText = it },
                                    placeholder = "Explique aqui...",
                                    enabled = !isFeynmanTyping
                                )
                                IconButton(
                                    onClick = {
                                        if (feynmanInputText.isNotBlank()) {
                                            onSendFeynmanMessage(feynmanInputText)
                                            feynmanInputText = ""
                                        }
                                    },
                                    enabled = feynmanInputText.isNotBlank() && !isFeynmanTyping,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = DarkNavyBlue,
                                        contentColor = White,
                                        disabledContainerColor = VeryLightGray
                                    ),
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_play_arrow),
                                        contentDescription = "Enviar",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    } else {
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
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
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

                        FlashcardType.CHAT_FEYNMAN -> {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                feynmanPersonaName?.let { name ->
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = LightNavyBlue.copy(alpha = 0.1f),
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    ) {
                                        Text(
                                            text = "Conversando com $name",
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkNavyBlue
                                        )
                                    }
                                }

                                feynmanChatMessages.forEach { message ->
                                    FeynmanChatBubble(message = message)
                                }

                                if (isFeynmanTyping) {
                                    FeynmanTypingIndicator(feynmanPersonaName ?: "IA")
                                }

                                if (feynmanErrorMessage != null) {
                                    Text(
                                        text = feynmanErrorMessage,
                                        color = Red,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                
                                if (isQuestionVerified) {
                                    Surface(
                                        color = Green.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            verticalAlignment = CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                painterResource(id = R.drawable.ic_check_circle),
                                                contentDescription = null,
                                                tint = Green,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Text(
                                                text = "Conceito aprendido! Sessão finalizada.",
                                                fontWeight = FontWeight.Bold,
                                                color = DarkNavyBlue
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                    HintReveal(hints = targetFlashcard.hints)
                }
            }
        }
    }
}

@Composable
fun FeynmanChatBubble(message: FeynmanChatMessage) {
    val arrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    val bubbleColor = if (message.isFromUser) DarkNavyBlue else White
    val textColor = if (message.isFromUser) White else DarkGray
    val shape = if (message.isFromUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement
    ) {
        Surface(
            shape = shape,
            color = bubbleColor,
            shadowElevation = 1.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                modifier = Modifier.padding(12.dp),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun FeynmanTypingIndicator(personaName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(DarkGray.copy(alpha = 0.5f))
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = "$personaName está pensando...",
            fontSize = 12.sp,
            color = DarkGray,
            fontWeight = FontWeight.Medium
        )
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
            isAnswerCorrect = true,
            elapsedTime = "00:00",
        )
    }
}
