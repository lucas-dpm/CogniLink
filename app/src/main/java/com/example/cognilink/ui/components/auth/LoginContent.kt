package com.example.cognilink.ui.components.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.White

@Composable
fun LoginContent(modifier: Modifier = Modifier,
                 onSignUpClick: () -> Unit = {}) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {

        TextInput(label = "E-MAIL", placeholder = "seu@email.com", keyboardType = KeyboardType.Email, inputValue = email, onInputValueChange = { email = it })

        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "SENHA",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = DarkGray,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "ESQUECEU A SENHA?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = DarkNavyBlue,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        /* TODO */
                    }
                )
            }
            PasswordInput(password = password, onPasswordChange = { password = it })
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
        ) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "ENTRAR",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = 10.dp)
                )
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_arrow_forward
                    ),
                    contentDescription = "Entrar",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier,
                )
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Ainda não tem uma conta?", modifier = Modifier.padding(end = 10.dp), color = DarkGray)
            Text(text = "Cadastre-se",
                color =  DarkNavyBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    onSignUpClick()
                })
        }

        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "OU CONTINUE COM", color = DarkGray, fontWeight = FontWeight.Bold)
        }

        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically)
        {
            Button(onClick = {/*TODO*/},
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.weight(1f).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp))
            }

            Button(onClick = {/*TODO*/},
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.weight(1f).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_apple_inc),
                    contentDescription = "Apple",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginContentPreview() {
    CogniLinkTheme{
        LoginContent()
    }
}