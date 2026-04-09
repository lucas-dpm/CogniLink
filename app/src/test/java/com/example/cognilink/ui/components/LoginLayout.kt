package com.example.cognilink.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import com.example.cognilink.ui.components.auth.EmailTextField
import com.example.cognilink.ui.components.auth.PasswordInput

@androidx.compose.runtime.Composable
fun LoginComponent(modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier.Companion) {
    var email by _root_ide_package_.androidx.compose.runtime.remember {
        _root_ide_package_.androidx.compose.runtime.mutableStateOf(
            ""
        )
    }
    var password by _root_ide_package_.androidx.compose.runtime.remember {
        _root_ide_package_.androidx.compose.runtime.mutableStateOf(
            ""
        )
    }

    _root_ide_package_.androidx.compose.foundation.layout.Column(
        modifier = androidx.compose.ui.Modifier.Companion.fillMaxWidth(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp)
    ) {
        EmailTextField(email = email, onEmailChange = { email = it })
        _root_ide_package_.androidx.compose.foundation.layout.Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = androidx.compose.ui.Alignment.Companion.Start,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            _root_ide_package_.androidx.compose.foundation.layout.Row(modifier = androidx.compose.ui.Modifier.Companion.fillMaxWidth()) {
                _root_ide_package_.androidx.compose.material3.Text(
                    text = "SENHA",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Companion.Bold
                    ),
                    color = _root_ide_package_.com.example.cognilink.ui.theme.DarkGray,
                    modifier = androidx.compose.ui.Modifier.Companion.weight(1f)
                )
                _root_ide_package_.androidx.compose.material3.Text(
                    text = "ESQUECEU A SENHA?",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Companion.Bold
                    ),
                    color = _root_ide_package_.com.example.cognilink.ui.theme.DarkNavyBlue,
                    modifier = androidx.compose.ui.Modifier.Companion.clickable(
                        interactionSource = _root_ide_package_.androidx.compose.runtime.remember { _root_ide_package_.androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) {
                        /* TODO */
                    }
                )
            }
            PasswordInput(password = password, onPasswordChange = { password = it })
        }

        _root_ide_package_.androidx.compose.material3.Button(
            onClick = { /*TODO*/ },
            modifier = androidx.compose.ui.Modifier.Companion
                .fillMaxWidth()
                .padding(top = 20.dp),
        ) {
            _root_ide_package_.androidx.compose.foundation.layout.Row(
                modifier = androidx.compose.ui.Modifier.Companion
                    .fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.Companion.CenterVertically
            ) {
                _root_ide_package_.androidx.compose.material3.Text(
                    text = "ENTRAR",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Companion.Bold
                    ),
                    modifier = androidx.compose.ui.Modifier.Companion.padding(end = 10.dp)
                )
                _root_ide_package_.androidx.compose.material3.Icon(
                    painter = _root_ide_package_.androidx.compose.ui.res.painterResource(
                        id = com.example.cognilink.R.drawable.ic_arrow_forward
                    ),
                    contentDescription = "Entrar",
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                    modifier = androidx.compose.ui.Modifier.Companion,
                )
            }
        }

        _root_ide_package_.androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier.Companion
                .fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            verticalAlignment = androidx.compose.ui.Alignment.Companion.CenterVertically
        ) {
            _root_ide_package_.androidx.compose.material3.Text(
                text = "Ainda não tem uma conta?",
                modifier = androidx.compose.ui.Modifier.Companion.padding(end = 10.dp)
            )
            _root_ide_package_.androidx.compose.material3.Text(
                text = "Cadastre-se",
                color = _root_ide_package_.com.example.cognilink.ui.theme.DarkNavyBlue,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Companion.Bold,
                modifier = androidx.compose.ui.Modifier.Companion.clickable(
                    interactionSource = _root_ide_package_.androidx.compose.runtime.remember { _root_ide_package_.androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null
                ) {
                    /* TODO */
                })
        }

        _root_ide_package_.androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier.Companion
                .fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            verticalAlignment = androidx.compose.ui.Alignment.Companion.CenterVertically
        ) {
            _root_ide_package_.androidx.compose.material3.Text(
                text = "OU CONTINUE COM",
                color = _root_ide_package_.com.example.cognilink.ui.theme.DarkGray,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Companion.Bold
            )
        }

        _root_ide_package_.androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier.Companion
                .fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(15.dp),
            verticalAlignment = androidx.compose.ui.Alignment.Companion.CenterVertically
        )
        {
            _root_ide_package_.androidx.compose.material3.Button(
                onClick = {/*TODO*/ },
                shape = _root_ide_package_.androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                modifier = androidx.compose.ui.Modifier.Companion.weight(1f).height(50.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color.Companion.White
                )
            ) {
                _root_ide_package_.androidx.compose.material3.Icon(
                    painter = _root_ide_package_.androidx.compose.ui.res.painterResource(id = com.example.cognilink.R.drawable.ic_google),
                    contentDescription = "Google",
                    tint = androidx.compose.ui.graphics.Color.Companion.Unspecified,
                    modifier = androidx.compose.ui.Modifier.Companion.size(30.dp)
                )
            }

            _root_ide_package_.androidx.compose.material3.Button(
                onClick = {/*TODO*/ },
                shape = _root_ide_package_.androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                modifier = androidx.compose.ui.Modifier.Companion.weight(1f).height(50.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color.Companion.White
                )
            ) {
                _root_ide_package_.androidx.compose.material3.Icon(
                    painter = _root_ide_package_.androidx.compose.ui.res.painterResource(id = com.example.cognilink.R.drawable.ic_apple_inc),
                    contentDescription = "Apple",
                    tint = androidx.compose.ui.graphics.Color.Companion.Unspecified,
                    modifier = androidx.compose.ui.Modifier.Companion.size(30.dp)
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@androidx.compose.runtime.Composable
private fun LoginComponentPreview() {
    _root_ide_package_.com.example.cognilink.ui.theme.CogniLinkTheme {
        LoginComponent()
    }
}