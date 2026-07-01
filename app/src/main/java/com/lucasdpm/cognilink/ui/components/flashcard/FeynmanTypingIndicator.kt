package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.ui.theme.DarkGray

@Composable
fun FeynmanTypingIndicator(personaName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(DarkGray.copy(alpha = 0.5f))
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = "$personaName está pensando...",
            fontSize = 12.sp,
            color = DarkGray,
            fontWeight = FontWeight.Medium
        )
    }
}
