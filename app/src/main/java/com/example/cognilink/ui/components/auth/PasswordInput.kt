package com.example.cognilink.ui.components.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.LightGray
import com.example.cognilink.ui.theme.White

@Composable
fun PasswordInput(modifier: Modifier = Modifier,
                  label: String = "",
                  password: String,
                  onPasswordChange: (String) -> Unit,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        if(label != "")
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = DarkGray
            )
        Surface(modifier = modifier, shape = RoundedCornerShape(24.dp), color = White, border = BorderStroke(1.5.dp,LightGray)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                OutlinedTextField(
                    value = password, onValueChange = onPasswordChange,
                    modifier = Modifier
                        .weight(1f),
                    textStyle = if (isPasswordVisible) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleMedium.copy(
                        color = LightGray
                    ),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        )
                )
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent
                    ),

                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                        ),
                        contentDescription = if (isPasswordVisible) "Esconder senha" else "Mostrar senha",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(end = 10.dp).clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ){ isPasswordVisible = !isPasswordVisible }
                    )
                }

            }
        }

    }



}

@Preview
@Composable
private fun PasswordInputPreview() {
    CogniLinkTheme {
        PasswordInput(password = "", onPasswordChange = {})
        //PasswordInput(label="Confirmar senha",password = "", onPasswordChange = {})
    }
}
