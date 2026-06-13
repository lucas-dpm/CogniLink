package com.lucasdpm.cognilink.ui.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun AuthHeader() {
    GradientSurface(shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 50.dp),
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
private fun AuthHeaderPreview() {
    CogniLinkTheme {
        AuthHeader()
    }
}