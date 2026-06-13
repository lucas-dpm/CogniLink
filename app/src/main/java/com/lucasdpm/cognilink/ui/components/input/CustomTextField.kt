package com.lucasdpm.cognilink.ui.components.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.ui.components.utils.labels.CustomLabel
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.LightGray
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: String,
    minLines: Int = 1,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorMessage: String? = null,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        if (label != null) {
            label()
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 2.dp,
            color = White,
            border = BorderStroke(
                width = 1.dp,
                color = if (errorMessage != null) MaterialTheme.colorScheme.error else Color.Transparent
            )
        )
        {
            Row(verticalAlignment = Alignment.CenterVertically)
            {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = onInputValueChange,
                    enabled = enabled,
                    modifier = modifier
                        .weight(1f),
                    textStyle = MaterialTheme.typography.titleMedium,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                    ),
                    minLines = minLines,
                    placeholder = {
                        Text(
                            text = placeholder,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = LightGray
                            )
                        )
                    }
                )
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }

}

@Preview
@Composable
private fun CustomTextFieldPreview() {
    CogniLinkTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomTextField(
                inputValue = "",
                onInputValueChange = {},
                label = { CustomLabel(text = "Nome", textColor = DarkGray) },
                placeholder = "Seu nome"
            )
            CustomTextField(
                inputValue = "Valor inválido",
                onInputValueChange = {},
                label = { CustomLabel(text = "E-mail", textColor = DarkGray) },
                placeholder = "seu@email.com",
                errorMessage = "E-mail inválido"
            )
        }
    }
}
