package com.lucasdpm.cognilink.ui.components.utils.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.VividCyan
import com.lucasdpm.cognilink.ui.theme.neonGlow


@Composable
fun NeonFAB(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    neonColor: Color = VividCyan,
    initialBackgroundColor: Color = DarkNavyBlue,
    finalBackgroundColor: Color = Color(0xFF1222B0),
    buttonDescription: String = "Adicionar",
    iconColor: Color = VividCyan,
    icon: Int = R.drawable.ic_add,
    onClick: () -> Unit,
) {
    val iconSize = size / 2

    Box(
        modifier = Modifier
            .neonGlow(
                color = neonColor,
                borderRadius = size / 2,
                glowRadius = 5.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        GradientSurface(
            shape = CircleShape,
            border = BorderStroke(2.dp, neonColor),
            initialColor = initialBackgroundColor,
            finalColor = finalBackgroundColor
        )
        {
            Box(
                modifier = modifier.size(size).clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = buttonDescription,
                    tint = iconColor,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}

@Preview
@Composable
private fun NeonButtonPreview() {
    NeonFAB(onClick = {})
}