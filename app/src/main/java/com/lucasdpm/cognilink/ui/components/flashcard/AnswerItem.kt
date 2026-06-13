package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.MutedBlue
import com.lucasdpm.cognilink.ui.theme.OffWhite

@Composable
fun AnswerItem(
    modifier: Modifier = Modifier,
    label: String,
    labelTextColor: Color = DarkNavyBlue,
    labelBackgroundColor: Color = OffWhite,
    answerText: String,
    onAnswerTextChange: (String) -> Unit = {},
    readOnly: Boolean = true,
    border: BorderStroke? = null,
    selectionControl: @Composable (() -> Unit)? = null,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 2.dp,
        border = border
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = labelBackgroundColor,
            ){
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = labelTextColor,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            }


            OutlinedTextField(
                value = answerText,
                onValueChange = onAnswerTextChange,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                placeholder = { Text("Digite a resposta...") },
                shape = CircleShape,
                readOnly = readOnly,
            )
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                if (selectionControl != null) {
                    selectionControl()
                }
            }

        }
    }
}

@Preview
@Composable
private fun AnswerItemPreview() {
    CogniLinkTheme {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

            var answerText by remember { mutableStateOf("") }

            var selectedRadio by remember { mutableStateOf(false) }

            AnswerItem(
                label = "A",
                labelBackgroundColor = if(selectedRadio) MutedBlue else OffWhite,
                border = if(selectedRadio) BorderStroke(color = DarkNavyBlue, width = 2.dp) else null,
                answerText = answerText,
                onAnswerTextChange = { answerText = it },
                readOnly = false,
                selectionControl = {
                    RadioButton(
                        selected = selectedRadio,
                        onClick = { selectedRadio = true },
                        colors = RadioButtonDefaults.colors(selectedColor = DarkNavyBlue),
                    )
                }
            )

            var selectedCheckBox by remember { mutableStateOf(false) }

            AnswerItem(
                label = "B",
                answerText = "Resposta 1",
                selectionControl = {
                    Checkbox(
                        checked = selectedCheckBox,
                        onCheckedChange = { selectedCheckBox = !selectedCheckBox },
                        colors = CheckboxDefaults.colors(checkedColor = DarkNavyBlue)
                    )
                }
            )
        }
    }
}
