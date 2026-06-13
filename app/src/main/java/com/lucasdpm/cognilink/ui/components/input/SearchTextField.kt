package com.lucasdpm.cognilink.ui.components.input

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun SearchTextField(modifier: Modifier = Modifier,
                    hintText: String = "Pesquisar...",
                    searchValue: String = "",
                    onSearchValueChange: (String) -> Unit = {}) {

    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 2.dp,
        color = White
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Pesquisar",
                tint = DarkGray.copy(alpha = 0.5f),
                modifier = Modifier.padding(start = 12.dp)
            )
            OutlinedTextField(
                value = searchValue, onValueChange = onSearchValueChange,
                modifier = Modifier
                    .weight(1f),
                textStyle = MaterialTheme.typography.titleMedium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                placeholder = {
                    Text(
                        text = hintText,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = DarkGray.copy(alpha = 0.5f)
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun SearchTextFieldPreview() {
    CogniLinkTheme {
        SearchTextField()
    }
}