package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.domain.repository.FeynmanChatMessage
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun FeynmanChatBubble(message: FeynmanChatMessage) {
    val arrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    val bubbleColor = if (message.isFromUser) DarkNavyBlue else White
    val textColor = if (message.isFromUser) White else DarkGray
    val shape = if (message.isFromUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement
    ) {
        Surface(
            shape = shape,
            color = bubbleColor,
            shadowElevation = 1.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                modifier = Modifier.padding(12.dp),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}
