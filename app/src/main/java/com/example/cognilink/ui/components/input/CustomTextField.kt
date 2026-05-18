package com.example.cognilink.ui.components.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.cognilink.ui.components.utils.labels.CustomLabel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.LightGray
import com.example.cognilink.ui.theme.White

@Composable
fun CustomTextField(modifier: Modifier = Modifier,
                    inputValue: String,
                    onInputValueChange: (String) -> Unit,
                    label: @Composable (() -> Unit)? = null,
                    placeholder: String,
                    minLines: Int = 1,
                    enabled: Boolean = true,
                    keyboardType: KeyboardType = KeyboardType.Text
) {

    Column(
        horizontalAlignment = Alignment.Start,
    )
    {
        if (label != null) {
            label()
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 2.dp,
            color = White
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
                        errorBorderColor = Color.Transparent,
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
    }

}

@Preview
@Composable
private fun CustomTextFieldPreview() {
    CogniLinkTheme{
        CustomTextField(
            inputValue = "", onInputValueChange = {},
            label = { CustomLabel(text = "Nome", textColor = DarkGray) },
            placeholder = "Seu nome")
    }
}
