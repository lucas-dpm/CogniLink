package com.lucasdpm.cognilink.ui.components.flashcard

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
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.ui.states.AnswerVisualState
import com.lucasdpm.cognilink.ui.states.border
import com.lucasdpm.cognilink.ui.states.labelBackgroundColor
import com.lucasdpm.cognilink.ui.states.labelTextColor
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue


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

            AnswerItem(
                label = generatedLabel,
                answerText = answer.answer,
                border = visualState.border,
                labelBackgroundColor = visualState.labelBackgroundColor,
                labelTextColor = visualState.labelTextColor,
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

    var toggleCorrect by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CogniLinkTheme {
            // Exemplo Múltipla Escolha (‘Single’)
            AnswerSelector(
                answerOptions = listaTeste,
                getVisualState = { answer ->
                    val isSelected = selectedAnswersRadio.contains(answer)
                    if (toggleCorrect) {
                        if (answer.isCorrect) AnswerVisualState.Correct
                        else if (isSelected) AnswerVisualState.Incorrect
                        else AnswerVisualState.Default
                    } else {
                        if (isSelected) AnswerVisualState.Selected else AnswerVisualState.Default
                    }
                },
                selectionControl = { answer, _ ->
                    if (!toggleCorrect) {
                        RadioButton(
                            selected = (selectedAnswersRadio.contains(answer)),
                            onClick = { selectedAnswersRadio = setOf(answer) },
                            enabled = !toggleCorrect,
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

                    if (toggleCorrect) {
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
                    if(!toggleCorrect) {
                        TrueFalseToggle(
                            currentValue = selectedAnswersMap[answer],
                            onToggle = { choice ->
                                selectedAnswersMap = selectedAnswersMap + (answer to choice)
                            },
                            enabled = !toggleCorrect
                        )
                    }
                },
            )

            Button(onClick = { toggleCorrect = !toggleCorrect }) {
                Text(text="Corrigir resposta")
            }
        }
    }
}
