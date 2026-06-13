package com.lucasdpm.cognilink.ui.components.utils.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.theme.Gray
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun SimpleGradientButton(
    modifier: Modifier = Modifier,
    text: String = "ADICIONAR FLASHCARD",
    height: Dp = 70.dp,
    icon: Int? = null,
    iconColor: Color = White,
    iconRightSide: Boolean = false,
    isEnabled: Boolean = true,
    onClickButton: () -> Unit,
) {
    GradientSurface(
        modifier = modifier,
        shape = RoundedCornerShape(26.dp),
        initialColor = if (isEnabled) Color(0xFF000666) else Gray,
        finalColor = if (isEnabled) Color(0xFF1222B0) else OffWhite,
    )
    {
        Row(
            modifier = Modifier
                .height(height).fillMaxWidth()
                .clickable(enabled = isEnabled, onClick = onClickButton),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (icon != null && !iconRightSide) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = iconColor,
                )
            }
            Text(
                text = text,
                color = if (isEnabled) White else Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 0.dp)
            )
            if (icon != null && iconRightSide) {

                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = iconColor,
                )

            }
        }
    }
}

@Preview
@Composable
private fun SimpleGradientButtonPreview() {
    SimpleGradientButton(
        text = "ADICIONAR FLASHCARD",
        height = 56.dp,
        onClickButton = { }
    )
}
