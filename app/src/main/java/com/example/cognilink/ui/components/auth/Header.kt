package com.example.cognilink.ui.components.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.White

@Composable
fun Header() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF000666), // Cor inicial
                        Color(0xFF1A237E)  // Cor final
                    ),
                    // Opcional: define a direção (padrão é da esquerda-topo para direita-baixo)
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ),
        color = Color.Transparent,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Domine seu",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                )
                Text(
                    text = "conhecimento.",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                )
                Text(
                    text = "Eleve sua mente.",
                    color = Color(0xFFBDC2FF),
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                )
            }
        }
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    CogniLinkTheme {
        Header()
    }
}