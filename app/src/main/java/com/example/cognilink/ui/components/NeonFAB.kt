package com.example.cognilink.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.VividCyan


fun Modifier.neonGlow(
    color: Color,
    borderRadius: Dp,
    glowRadius: Dp = 20.dp
) = this.drawBehind {
    val paint = Paint().apply {
        val nativePaint = asFrameworkPaint()
        nativePaint.isAntiAlias = true
        // O segredo está aqui: um MaskFilter de Blur (Nativo do Android)
        nativePaint.maskFilter = android.graphics.BlurMaskFilter(
            glowRadius.toPx(),
            android.graphics.BlurMaskFilter.Blur.OUTER // Desenha apenas fora
        )
        nativePaint.color = color.toArgb()
    }

    drawIntoCanvas { canvas ->
        // Desenha a forma do botão (arredondada) com o paint desfocado
        canvas.drawRoundRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
            radiusX = borderRadius.toPx(),
            radiusY = borderRadius.toPx(),
            paint = paint
        )
    }
}

@Composable
fun NeonFAB(
    modifier: Modifier = Modifier,
    neonColor: Color = VividCyan,
    backgroundColor: Color = DarkNavyBlue,
    buttonDescription: String = "Adicionar",
    iconColor: Color = VividCyan,
    icon: Int = R.drawable.ic_add,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .neonGlow(
                color = neonColor,
                borderRadius = 28.dp,
                glowRadius = 5.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = backgroundColor,
            border = BorderStroke(2.dp, neonColor),
            shadowElevation = 0.dp,
        ) {
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = buttonDescription,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
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