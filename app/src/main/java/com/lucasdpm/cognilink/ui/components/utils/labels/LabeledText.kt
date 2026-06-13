package com.lucasdpm.cognilink.ui.components.utils.labels

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue

@Composable
fun LabeledText(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    labelColor: Color = DarkNavyBlue,
    textColor: Color = DarkGray,
    textSize: TextUnit = 12.sp,
    lineHeight: TextUnit = 14.sp
) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = labelColor, fontWeight = FontWeight.Bold)) {
            append(label)
        }
        append(text)
    }
    Text(
        text = annotatedString,
        color = textColor,
        fontSize = textSize,
        modifier = modifier.fillMaxWidth(),
        lineHeight = lineHeight
    )
}