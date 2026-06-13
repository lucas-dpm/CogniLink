package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.Green
import com.lucasdpm.cognilink.ui.theme.Red


@Composable
fun TrueFalseToggle(
    currentValue: String?,
    onToggle: (String) -> Unit,
    enabled: Boolean = true
) {
    val backgroundColor = when (currentValue) {
        "T" -> Green
        "F" -> Red
        else -> Color.LightGray.copy(alpha = 0.5f)
    }


    val icon = when (currentValue) {
        "T" -> R.drawable.ic_check
        "F" -> R.drawable.ic_close
        else -> null
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable {
                if (!enabled) return@clickable
                val nextValue = if (currentValue == "T") "F" else "T"
                onToggle(nextValue)
            },
        contentAlignment = Alignment.Center,
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )

        } else {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(DarkNavyBlue.copy(alpha = 0.8f))
            )
        }
    }
}

@Preview
@Composable
private fun TrueFalseTogglePreview() {
    var currentValue by remember { mutableStateOf<String?>(null) }

    TrueFalseToggle(
        currentValue,
        onToggle = { currentValue = if (currentValue == "T") "F" else "T" }
    )


}