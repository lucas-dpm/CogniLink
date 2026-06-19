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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.ui.components.utils.buttons.DeleteButton
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun TopicsItem(
    modifier: Modifier = Modifier,
    topic: String,
    onTopicChange: (String) -> Unit = {},
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
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = topic,
                    onValueChange = onTopicChange,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    placeholder = { Text("Digite o tópico...") },
                )
            }
            DeleteButton(onClick = onClickToRemove)
        }
    }
}

@Preview
@Composable
private fun TopicsItemPreview() {
    var topic by remember { mutableStateOf("Teste") }

    TopicsItem(topic = topic, onTopicChange = { topic = it }, onClickToRemove = {})
}