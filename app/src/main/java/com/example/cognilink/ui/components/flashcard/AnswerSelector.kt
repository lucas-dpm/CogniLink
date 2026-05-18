package com.example.cognilink.ui.components.flashcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.domain.Answer
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Green
import com.example.cognilink.ui.theme.MutedBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.Red


sealed interface AnswerVisualState {
    object Default : AnswerVisualState
    object Selected : AnswerVisualState
    object Correct : AnswerVisualState
    object Incorrect : AnswerVisualState
}

@Composable
fun AnswerSelector(
    modifier: Modifier = Modifier,
    answerOptions: List<Answer>,
    getVisualState: (Answer) -> AnswerVisualState = { AnswerVisualState.Default },
    selectionControl: @Composable ((answer: Answer, index: Int) -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        answerOptions.forEachIndexed { index, answer ->
            val generatedLabel = ('A' + index).toString()

            val visualState = getVisualState(answer)

            val cardBorder = when (visualState) {
                AnswerVisualState.Selected -> BorderStroke(2.dp, DarkNavyBlue)
                AnswerVisualState.Correct -> BorderStroke(2.dp, Green)
                AnswerVisualState.Incorrect -> BorderStroke(2.dp, Red)
                AnswerVisualState.Default -> null
            }

            val labelBackgroundColor = when (visualState) {
                AnswerVisualState.Selected -> MutedBlue
                AnswerVisualState.Correct -> Green.copy(alpha = 0.2f)
                AnswerVisualState.Incorrect -> Red.copy(alpha = 0.2f)
                AnswerVisualState.Default -> OffWhite
            }

            val labelTextColor = when (visualState) {
                AnswerVisualState.Correct -> Green
                AnswerVisualState.Incorrect -> Red
                else -> DarkNavyBlue
            }

            AnswerItem(
                label = generatedLabel,
                answerText = answer.answer,
                border = cardBorder,
                labelBackgroundColor = labelBackgroundColor,
                labelTextColor = labelTextColor,
                readOnly = true,
                selectionControl = if (selectionControl != null) {
                    { selectionControl(answer,index) }
                } else null
            )
        }
    }
}

@Preview
@Composable
private fun AnswerSelectorPreview() {
    val listaTeste = listOf(
        Answer("Resposta 1", true),
        Answer("Resposta 2", false),
        Answer("Resposta 3", false)
    )

    var selectedAnswersRadio by remember { mutableStateOf(setOf<Answer>()) }

    var selectedAnswersMap by remember { mutableStateOf(mapOf<Answer, String>()) }

    var toogleCorrect by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CogniLinkTheme {
            // Exemplo Múltipla Escolha (Single)
            AnswerSelector(
                answerOptions = listaTeste,
                getVisualState = { answer ->
                    val isSelected = selectedAnswersRadio.contains(answer)
                    if (toogleCorrect) {
                        if (answer.isCorrect) AnswerVisualState.Correct
                        else if (isSelected) AnswerVisualState.Incorrect
                        else AnswerVisualState.Default
                    } else {
                        if (isSelected) AnswerVisualState.Selected else AnswerVisualState.Default
                    }
                },
                selectionControl = { answer, _ ->
                    if (!toogleCorrect) {
                        RadioButton(
                            selected = (selectedAnswersRadio.contains(answer)),
                            onClick = { selectedAnswersRadio = setOf(answer) },
                            enabled = !toogleCorrect,
                            colors = RadioButtonDefaults.colors(selectedColor = DarkNavyBlue),
                        )
                    }
                }
            )


            // Exemplo Verdadeiro ou Falso (Multiple)
            AnswerSelector(
                answerOptions = listaTeste,
                getVisualState = { answer ->
                    val userChoice = selectedAnswersMap[answer]
                    val isAnswerCorrectType = answer.isCorrect

                    if (toogleCorrect) {
                        when {
                            (userChoice == "V" && isAnswerCorrectType) || (userChoice == "F" && !isAnswerCorrectType) -> {
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
                    if(!toogleCorrect) {
                        TrueFalseToggle(
                            currentValue = selectedAnswersMap[answer],
                            onToggle = { choice ->
                                selectedAnswersMap = selectedAnswersMap + (answer to choice)
                            },
                            enabled = !toogleCorrect
                        )
                    }
                },
            )

            Button(onClick = { toogleCorrect = !toogleCorrect }) {
                Text(text="Corrigir resposta")
            }
        }
    }
}

