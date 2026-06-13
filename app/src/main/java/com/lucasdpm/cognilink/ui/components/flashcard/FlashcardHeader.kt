package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.components.utils.ProgressBar
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.VividCyan
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun FlashcardHeader(modifier: Modifier = Modifier,
                    title: String = "REVISÃO",
                    actualCard: Int = 12,
                    totalCards: Int = 50,
                    onCloseClick: () -> Unit = {},
) {

    GradientSurface()
    {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onCloseClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Fechar",
                        tint = White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "SESSÃO DE ESTUDO",
                        color = VividCyan,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
            if(totalCards > 1)
            // Bloco da Direita: Contador + Barra de Progresso
            {
                Column(
                    modifier = Modifier.width(120.dp), // Largura fixa para a barra não variar
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "$actualCard / $totalCards",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )

                    ProgressBar(
                        progress = actualCard / totalCards.toFloat(),
                        progressColor = White,
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun FlashcardHeaderPreview() {
    CogniLinkTheme{
        FlashcardHeader()
    }
}