package com.example.cognilink.ui.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.CogniLinkTheme


@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    confirmPassword: String = "",
    onConfirmPasswordChange: (String) -> Unit = {},
    name: String = "",
    onNameChange: (String) -> Unit = {},
) {

    var email by remember { mutableStateOf(email) }
    var password by remember { mutableStateOf(password) }
    var confirmPassword by remember { mutableStateOf(confirmPassword) }
    var name by remember { mutableStateOf(name) }


    Column(modifier = Modifier,verticalArrangement = Arrangement.spacedBy(20.dp))
    {
        TextInput(label = "NOME", placeholder = "Seu nome", inputValue = name, onInputValueChange = { name = it })

        TextInput(label = "E-MAIL", placeholder = "seu@email.com", inputValue = email, onInputValueChange = { email = it })

        PasswordInput(label = "CRIAR SENHA",password = password, onPasswordChange = { password = it })

        PasswordInput(label = "CONFIRMAR SENHA",password = confirmPassword, onPasswordChange = { confirmPassword = it })

        TermsCheckbox()

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "CRIAR CONTA",
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
    }
}

@Preview
@Composable
private fun SignUpContentPreview() {
    CogniLinkTheme {
        SignUpContent()
    }
}