package com.example.cognilink.ui.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.components.input.PasswordTextField
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.components.utils.labels.CustomLabel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray

@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    name: String = "",
    onNameChange: (String) -> Unit = {},
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    confirmPassword: String = "",
    onConfirmPasswordChange: (String) -> Unit = {},
    isTermsAccepted: Boolean = false,
    onTermsAcceptedChange: (Boolean) -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CustomTextField(
            label = {
                CustomLabel(
                    text = "NOME",
                    textColor = DarkGray
                )
            },
            placeholder = "Seu nome",
            inputValue = name,
            onInputValueChange = onNameChange
        )

        CustomTextField(
            label = {
                CustomLabel(
                    text = "E-MAIL",
                    textColor = DarkGray
                )
            },
            placeholder = "seu@email.com",
            inputValue = email,
            onInputValueChange = onEmailChange
        )

        PasswordTextField(
            label = "CRIAR SENHA",
            password = password,
            onPasswordChange = onPasswordChange
        )

        PasswordTextField(
            label = "CONFIRMAR SENHA",
            password = confirmPassword,
            onPasswordChange = onConfirmPasswordChange
        )

        TermsCheckbox(
            checkedState = isTermsAccepted,
            onCheckedChange = onTermsAcceptedChange
        )

        SimpleGradientButton(
            text = "CRIAR CONTA",
            height = 40.dp,
            icon = R.drawable.ic_arrow_forward,
            iconRightSide = true,
            isEnabled = isTermsAccepted,
            onClickButton = onSignUpClick
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpContentPreview() {
    CogniLinkTheme {
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var isTermsAccepted by remember { mutableStateOf(false) }

        SignUpContent(
            name = name,
            onNameChange = { name = it },
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            confirmPassword = confirmPassword,
            onConfirmPasswordChange = { confirmPassword = it },
            isTermsAccepted = isTermsAccepted,
            onTermsAcceptedChange = { isTermsAccepted = it }
        )
    }
}
