package com.lucasdpm.cognilink.ui.components.utils.labels

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdpm.cognilink.ui.theme.DarkGray

@Composable
fun CustomLabel(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = DarkGray,
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = modifier
    )
}
@Preview(showBackground = true)
@Composable
private fun CustomLabelPreview() {
    CustomLabel(text = "Label Test")
}
