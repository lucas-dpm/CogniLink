package com.lucasdpm.cognilink.ui.components.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.input.PasswordTextField
import com.lucasdpm.cognilink.ui.components.input.CustomTextField
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.components.utils.labels.CustomLabel
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue

@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    name: String = "",
    onNameChange: (String) -> Unit = {},
    nameError: String? = null,
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    emailError: String? = null,
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    passwordError: String? = null,
    confirmPassword: String = "",
    onConfirmPasswordChange: (String) -> Unit = {},
    confirmPasswordError: String? = null,
    isTermsAccepted: Boolean = false,
    onTermsAcceptedChange: (Boolean) -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onTermsClick: () -> Unit = {}
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
            onInputValueChange = onNameChange,
            errorMessage = nameError
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
            onInputValueChange = onEmailChange,
            errorMessage = emailError
        )

        PasswordTextField(
            label = "CRIAR SENHA",
            password = password,
            onPasswordChange = onPasswordChange,
            errorMessage = passwordError
        )

        PasswordTextField(
            label = "CONFIRMAR SENHA",
            password = confirmPassword,
            onPasswordChange = onConfirmPasswordChange,
            errorMessage = confirmPasswordError
        )

        val annotatedString = buildAnnotatedString {
            append("Li e concordo com os ")

            // Estilo para Termos de Uso
            pushStringAnnotation(tag = "TERMS", annotation = "")
            withStyle(style = SpanStyle(color = DarkNavyBlue, fontWeight = FontWeight.Bold)) {
                append("Termos de Uso")
            }
            pop()

            append(" e a ")

            // Estilo para Política de Privacidade
            pushStringAnnotation(tag = "PRIVACY", annotation = "")
            withStyle(style = SpanStyle(color = DarkNavyBlue, fontWeight = FontWeight.Bold)) {
                append("Política de Privacidade")
            }
            pop()

            append(".")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isTermsAccepted,
                onCheckedChange = onTermsAcceptedChange,
                colors = CheckboxDefaults.colors(checkedColor = DarkNavyBlue)
            )

            Text(
                text = annotatedString,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier.clickable { onTermsClick() }
            )
        }

        SimpleGradientButton(
            text = "CRIAR CONTA",
            height = 40.dp,
            icon = R.drawable.ic_arrow_forward,
            iconRightSide = true,
            isEnabled = isTermsAccepted,
            onClickButton = onSignUpClick,
            modifier = Modifier.padding(bottom = 12.dp)
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
