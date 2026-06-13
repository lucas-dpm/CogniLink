package com.lucasdpm.cognilink.ui.components.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.VividCyan

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    progress: Float = 0.50f,
    progressColor: Color = VividCyan,
    backgroundColor: Color = Color.White.copy(alpha = 0.3f)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        if (progress > 0f) {
            Surface(
                modifier = Modifier
                    .weight(progress)
                    .fillMaxHeight(),
                color = progressColor,
                shape = CircleShape
            ) {}
        }

        if (progress < 1f) {
            Spacer(modifier = Modifier.weight(1f - progress))
        }
    }
}

@Preview
@Composable
private fun ProgressBarPreview() {
    CogniLinkTheme {
        ProgressBar()
    }
}