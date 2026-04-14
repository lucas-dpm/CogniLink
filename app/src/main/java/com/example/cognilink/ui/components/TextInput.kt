package com.example.cognilink.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.LightGray
import com.example.cognilink.ui.theme.White

@Composable
fun TextInput(modifier: Modifier = Modifier,
              inputValue: String,
              onInputValueChange: (String) -> Unit,
              label: String,
              placeholder: String,
              keyboardType: KeyboardType = KeyboardType.Text
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = DarkGray
        )
        Surface(modifier = modifier,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.5.dp, LightGray),
            shadowElevation = 2.dp)
        {
            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically)
            {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = onInputValueChange,
                    modifier = Modifier
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
private fun TextInputPreview() {
    CogniLinkTheme{
        TextInput(inputValue = "", onInputValueChange = {}, label = "Nome", placeholder = "Seu nome")
    }
}
