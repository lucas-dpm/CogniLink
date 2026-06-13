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
fun HintEditor(
    modifier: Modifier = Modifier,
    hints: List<String> = emptyList(),
    onHintsUpdate: (List<String>) -> Unit,
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        hints.forEachIndexed { index, hint ->
            val label = "Dica ${index + 1}"
            HintItem(
                label = label,
                hint = hint,
                onHintChange = { newValue ->
                    if (index >= 0 && index < hints.size) {
                        val newList = hints.toMutableList()
                        newList[index] = newValue
                        onHintsUpdate(newList)
                    }
                },
                readOnly = false,
                onClickToRemove = {
                    if (index >= 0 && index < hints.size) {
                        val newList = hints.toMutableList()
                        newList.removeAt(index)
                        onHintsUpdate(newList)
                    }
                }
            )
        }

        if (hints.size < 3) {
            GradientSurface(
                border = BorderStroke(1.dp, LightGray),
                shape = RoundedCornerShape(50)
            ){
                TextButton(
                    onClick = {
                        val newList = hints.toMutableList()
                        newList.add("")
                        onHintsUpdate(newList)
                              },
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Adicionar dica (${hints.size + 1}/3)", color = White)
                }
            }
        }
    }
}

@Preview
@Composable
private fun HintEditorPreview() {
    //var listaTeste by remember { mutableStateOf(emptyList<String>()) }
    var listaTeste by remember { mutableStateOf(listOf("Teste 1", "Teste 2")) }

    CogniLinkTheme {
        HintEditor(
            hints = listaTeste,
            onHintsUpdate = { listaTeste = it }
        )
    }
}