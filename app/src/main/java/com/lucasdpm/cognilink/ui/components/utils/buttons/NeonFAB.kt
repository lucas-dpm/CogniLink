package com.lucasdpm.cognilink.ui.components.utils.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.VividCyan
import com.lucasdpm.cognilink.ui.theme.White
import com.lucasdpm.cognilink.ui.theme.neonGlow


@Composable
fun NeonFAB(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    neonColor: Color = VividCyan,
    initialBackgroundColor: Color = DarkNavyBlue,
    finalBackgroundColor: Color = Color(0xFF1222B0),
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val borderRadius = size / 2

    Box(
        modifier = modifier
            .size(size)
            .neonGlow(
                color = neonColor,
                borderRadius = borderRadius,
                glowRadius = 5.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        GradientSurface(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .clickable(
                    onClick = onClick,
                    role = Role.Button,
                    enabled = enabled
                ),
            shape = CircleShape,
            border = BorderStroke(2.dp, neonColor),
            initialColor = if(enabled) {
                initialBackgroundColor
            } else {
                White
            } ,
            finalColor = if(enabled) {
                finalBackgroundColor
            } else {
                White.copy(alpha = 0.7f)
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
        }
    }
}

@Preview
@Composable
private fun NeonButtonPreview() {
    NeonFAB(
        onClick = {},
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = Color.White
            )
        },
    )
}