package com.example.cognilink.ui.components.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkNavyBlue

@Composable
fun TermsCheckbox() {
    var checkedState by remember { mutableStateOf(false) }

    val annotatedString = buildAnnotatedString {
        append("Concordo com os ")

        // Estilo para Termos de Serviço
        pushStringAnnotation(tag = "TERMS", annotation = "")
        withStyle(style = SpanStyle(color = DarkNavyBlue, fontWeight = FontWeight.Bold)) {
            append("Termos de Serviço")
        }
        pop()

        append(" e a ")

        // Estilo para Política de Privacidade
        pushStringAnnotation(tag = "PRIVACY", annotation = "")
        withStyle(style = SpanStyle(color = DarkNavyBlue, fontWeight = FontWeight.Bold)) {
            append("Política de Privacidade")
        }
        pop()

        append(".")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = { checkedState = it },
            colors = CheckboxDefaults.colors(checkedColor = DarkNavyBlue)
        )

        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier.clickable() {
                /* TODO */
            }
        )
    }
}

@Preview
@Composable
private fun TermsCheckboxPreview() {
    CogniLinkTheme {
        TermsCheckbox()
    }
}