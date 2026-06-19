package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.LightGray
import com.lucasdpm.cognilink.ui.theme.White


@Composable
fun TopicsEditor(
    modifier: Modifier = Modifier,
    topics: List<String> = emptyList(),
    onTopicsUpdate: (List<String>) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val displayedTopics = if (isExpanded || topics.size <= 3) topics else topics.take(3)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        displayedTopics.forEachIndexed { index, topic ->
            TopicsItem(
                topic = topic,
                onTopicChange = { newValue ->
                    if (index >= 0 && index < topics.size) {
                        val newList = topics.toMutableList()
                        newList[index] = newValue
                        onTopicsUpdate(newList)
                    }
                },
                onClickToRemove = {
                    if (index >= 0 && index < topics.size) {
                        val newList = topics.toMutableList()
                        newList.removeAt(index)
                        onTopicsUpdate(newList)
                    }
                }
            )
        }

        if (topics.size > 3) {
            TextButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = if (isExpanded) "Ver menos" else "Ver mais (${topics.size - 3} extras)",
                    color = LightGray
                )
            }
        }

        if (topics.size < 10) {
            GradientSurface(
                border = BorderStroke(1.dp, LightGray),
                shape = RoundedCornerShape(50)
            ){
                TextButton(
                    onClick = {
                        val newList = topics.toMutableList()
                        newList.add("")
                        onTopicsUpdate(newList)
                        isExpanded = true
                    },
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Adicionar tópico (${topics.size + 1}/10)", color = White)
                }
            }
        }
    }
}

@Preview
@Composable
private fun TopicsEditorPreview() {
    //var listaTeste by remember { mutableStateOf(emptyList<String>()) }
    var listaTeste by remember { mutableStateOf(listOf("Teste 1", "Teste 2", "Teste 3","Teste 1", "Teste 2", "Teste 3")) }

    CogniLinkTheme {
        TopicsEditor (
            topics = listaTeste,
            onTopicsUpdate = { listaTeste = it }
        )
    }
}