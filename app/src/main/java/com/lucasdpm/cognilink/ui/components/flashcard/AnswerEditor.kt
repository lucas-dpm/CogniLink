package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.components.utils.buttons.DeleteButton
import com.lucasdpm.cognilink.ui.states.AnswerVisualState
import com.lucasdpm.cognilink.ui.states.border
import com.lucasdpm.cognilink.ui.states.labelBackgroundColor
import com.lucasdpm.cognilink.ui.states.labelTextColor
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.Green
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun AnswerEditor(
    modifier: Modifier = Modifier,
    answerOptions: List<Answer>,
    onAnswerOptionsUpdate: (List<Answer>) -> Unit,
    getVisualState: (Answer) -> AnswerVisualState = { AnswerVisualState.Default },
    selectionControl: @Composable ((answer: Answer, index: Int) -> Unit)? = null,
    limit: Int = 5
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
                onAnswerTextChange = { newText ->
                    val newList = answerOptions.toMutableList()
                    newList[index] = newList[index].copy(answer = newText)
                    onAnswerOptionsUpdate(newList)
                },
                border = visualState.border,
                labelBackgroundColor = visualState.labelBackgroundColor,
                labelTextColor = visualState.labelTextColor,
                readOnly = false,
                selectionControl = if (selectionControl != null) {
                    { selectionControl(answer,index) }
                } else null
            )
        }

        if (answerOptions.size < limit) {
            GradientSurface(
                shape = RoundedCornerShape(50),
                shadowElevation = 1.dp
            ) {
                TextButton(
                    onClick = {
                        val newList = answerOptions.toMutableList()
                        newList.add(Answer("", false))
                        onAnswerOptionsUpdate(newList)
                    },
                    shape = RoundedCornerShape(50),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Adicionar opção de resposta (${answerOptions.size + 1}/$limit)",
                        color = White)
                }
            }
        }
    }
}

@Preview
@Composable
private fun AnswerEditorPreview() {

    //var listaTeste1 by remember { mutableStateOf(listOf<Answer>())}
    var listaTeste1 by remember { mutableStateOf(listOf(Answer("Resposta 1", false), Answer("Resposta 2", false), Answer("Resposta 3", false))) }
    var listaTeste2 by remember { mutableStateOf(listOf(Answer("Resposta 1", true), Answer("Resposta 2", false), Answer("Resposta 3", false))) }
    var removeToggle by remember { mutableStateOf(false) }


    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CogniLinkTheme {
            AnswerEditor(
                answerOptions = listaTeste1,
                onAnswerOptionsUpdate = { listaTeste1 = it },
                selectionControl = {answer , index ->
                    if (removeToggle){
                        DeleteButton(
                            onClick = {
                                val newList = listaTeste1.toMutableList()
                                newList.remove(answer)
                                listaTeste1 = newList
                            }
                        )
                    }
                    else{
                        RadioButton(
                            selected = answer.isCorrect,
                            onClick = { listaTeste1 = listaTeste1.mapIndexed { i, a -> a.copy(isCorrect = i == index) } },
                            colors = RadioButtonDefaults.colors(selectedColor = Green),
                        )
                    }
                },
                getVisualState = {answer ->
                    if (answer.isCorrect) AnswerVisualState.Correct
                    else AnswerVisualState.Incorrect
                }
            )

            AnswerEditor(
                answerOptions = listaTeste2,
                onAnswerOptionsUpdate = { listaTeste2 = it },
                selectionControl = {
                        answer, index ->
                    if (removeToggle){
                        DeleteButton(
                            onClick = {
                                val newList = listaTeste2.toMutableList()
                                newList.remove(answer)
                                listaTeste2 = newList
                            }
                        )
                    } else
                        TrueFalseToggle(
                            currentValue = if (answer.isCorrect) "T" else "F",
                            onToggle = { _ ->
                                listaTeste2 = listaTeste2.mapIndexed { i, a ->
                                    if (i == index) a.copy(isCorrect = !a.isCorrect)
                                    else
                                        a.copy(isCorrect = a.isCorrect)
                                }
                            }
                        )
                },
                getVisualState = {answer ->
                    if (answer.isCorrect) AnswerVisualState.Correct
                    else AnswerVisualState.Incorrect
                }
            )

            Button(onClick = { removeToggle = !removeToggle }) {
                Text(text="REMOVER OPCAO DE RESPOSTA")
            }
        }
    }
}
