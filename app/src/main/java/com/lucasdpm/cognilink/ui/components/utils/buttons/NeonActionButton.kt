package com.lucasdpm.cognilink.ui.components.utils.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.Gray
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.theme.White
import com.lucasdpm.cognilink.ui.theme.neonGlow

@Composable
fun NeonActionButton(
    modifier: Modifier = Modifier,
    text: String = "ADICIONAR FLASHCARD",
    height: Dp = 70.dp,
    icon: Int? = null,
    isEnabled: Boolean = true,
    iconRightSide: Boolean = false,
    onClickButton: () -> Unit
) {
    GradientSurface(
        modifier = modifier,
        initialColor = if (isEnabled) Color(0xFF000666) else Gray,
        finalColor = if (isEnabled) Color(0xFF1222B0) else OffWhite,
        shape = RoundedCornerShape(28.dp),
    ) {
        Row(
            modifier = Modifier.height(height)
                .clickable(enabled = isEnabled, onClick = onClickButton),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null && !iconRightSide) {
                Surface(
                    shape = CircleShape,
                    color = White,
                    modifier = Modifier.neonGlow(White, 24.dp, 5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = DarkNavyBlue,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            Text(
                text = text,
                color = White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 0.dp)
            )
            if (icon != null && iconRightSide) {
                Surface(
                    shape = CircleShape,
                    color = White,
                    modifier = Modifier.neonGlow(White, 24.dp, 5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = DarkNavyBlue,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
    
}

@Preview
@Composable
private fun NeonActionButtonPreview() {
    NeonActionButton(icon = R.drawable.ic_arrow_forward, iconRightSide = false, onClickButton = {})
}