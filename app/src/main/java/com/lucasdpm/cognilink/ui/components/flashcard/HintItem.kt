package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.ui.components.utils.buttons.DeleteButton
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.MutedBlue
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun HintItem(
    modifier: Modifier = Modifier,
    label: String,
    hint: String,
    readOnly: Boolean = true,
    onHintChange: (String) -> Unit = {},
    onClickToRemove: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 2.dp,
        color = White
    ) {
        Row(
            modifier = modifier.padding(vertical = 0.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MutedBlue,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavyBlue,
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = hint,
                    onValueChange = onHintChange,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        ),
                    placeholder = { Text("Digite a dica...") },
                    readOnly = readOnly
                )
            }
            if (!readOnly) {
                DeleteButton(onClick = onClickToRemove)
            }
        }
    }
}

@Preview
@Composable
private fun HintItemPreview() {
    var hint by remember { mutableStateOf("Teste") }

    HintItem(label = "Dica 1", hint = hint, onHintChange = { }, onClickToRemove = {})
}