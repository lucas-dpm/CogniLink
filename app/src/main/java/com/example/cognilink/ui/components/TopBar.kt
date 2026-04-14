package com.example.cognilink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.VividCyan

@Composable
fun TopBar(modifier: Modifier = Modifier,
           title: String = "TOP BAR",
           onBackClick: () -> Unit = {},
           MenuEnabled: Boolean = false,
           onMenuClick: () -> Unit = {},
) {

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
                    )
                ),
            color = Color.Transparent,
        ) {
            Row(
                modifier = modifier.fillMaxWidth().height(64.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Voltar",
                    tint = VividCyan,
                    modifier = Modifier
                )
            }

            Text(text = title,
                color = VividCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f))

            if(MenuEnabled){
                IconButton(onClick = onMenuClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Menu",
                        tint = VividCyan
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    CogniLinkTheme{
        TopBar()
    }
}