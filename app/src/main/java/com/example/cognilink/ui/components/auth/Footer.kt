package com.example.cognilink.ui.components.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LightNavyBlue
import com.example.cognilink.ui.theme.VeryLightGray

@Composable
fun Footer() {

    Surface(modifier = Modifier,
        color = VeryLightGray,
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
    ))
    {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 25.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(shape = RoundedCornerShape(24.dp)) {
                Row(modifier = Modifier
                    .background(LightNavyBlue)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = "Cadeado",
                        tint = DarkNavyBlue
                    )
                    Text(text = "SESSÃO SEGURA CRIPTOGRAFADA",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavyBlue
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = "Política de Privacidade",
                    color = DarkGray,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        /* TODO */
                    })
                Icon(painter = painterResource(id = R.drawable.ic_overlay),
                    contentDescription = "ponto",
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
                Text(text = "Termos de Uso",
                    color = DarkGray,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        /* TODO */
                    })
            }
        }
    }

}

@Preview
@Composable
private fun FooterPreview() {
    CogniLinkTheme {
        Footer()
    }
}