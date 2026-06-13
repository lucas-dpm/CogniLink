package com.lucasdpm.cognilink.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp = 2.dp,
    dashWidth: Dp = 8.dp,
    gapWidth: Dp = 4.dp,
    cornerRadius: Dp = 0.dp
) = this.drawBehind {
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashWidth.toPx(), gapWidth.toPx()),
            phase = 0f
        )
    )

    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = CornerRadius(cornerRadius.toPx())
    )
}