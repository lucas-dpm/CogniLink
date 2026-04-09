package com.example.cognilink.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.ui.components.auth.Footer
import com.example.cognilink.ui.components.auth.Header
import com.example.cognilink.ui.components.auth.LoginContent
import com.example.cognilink.ui.components.auth.SignUpContent
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VeryLightGray

@Composable
fun AuthScreen() {
    
}

@Composable
fun AuthContent(
    authOption: Boolean = false
) {
    var optionState by remember { mutableStateOf(authOption) }

    // Cria o estado da rolagem
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = OffWhite),
    ) {
        Header()

        // Seletor (Entrar / Cadastrar)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-30).dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = VeryLightGray,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { optionState = false },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!optionState) Color.White else Color.Transparent
                        )
                    ) {
                        Text("ENTRAR", fontWeight = FontWeight.Bold, color = if (!optionState) DarkNavyBlue else DarkGray)
                    }
                    Button(
                        onClick = { optionState = true },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (optionState) Color.White else Color.Transparent
                        )
                    ) {
                        Text("CADASTRAR", fontWeight = FontWeight.Bold, color = if (optionState) DarkNavyBlue else DarkGray)
                    }
                }
            }
        }

        //Column interna que ocupará o espaço restante e terá scroll
        Column(
            modifier = Modifier
                .weight(1f) // Faz esta coluna ocupar todo o espaço entre Header e Footer
                .verticalScroll(scrollState) // Habilita a rolagem vertical
        ) {

            // Layouts de Login/Cadastro
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                if (optionState) {
                    SignUpContent()
                } else {
                    LoginContent(onSignUpClick = { optionState = true })
                }
            }
        }

        //Footer fica fora da área de scroll para estar sempre visível
        Footer()
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthContentPreview() {
    CogniLinkTheme{
        AuthContent(authOption = false)
    }
}