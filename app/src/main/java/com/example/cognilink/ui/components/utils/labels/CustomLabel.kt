package com.example.cognilink.ui.components.utils.labels

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cognilink.ui.theme.DarkGray

@Composable
fun CustomLabel(
    text: String,
    textColor: Color = DarkGray,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = modifier.padding(bottom = 4.dp)
    )
}