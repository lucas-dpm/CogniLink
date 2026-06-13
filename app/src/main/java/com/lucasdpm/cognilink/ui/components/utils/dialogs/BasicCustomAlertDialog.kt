package com.lucasdpm.cognilink.ui.components.utils.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicCustomAlertDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit,
    confirmationButtonText: String,
    dismissButtonText: String? = null,
    dialogTitle: String,
    dialogText: String? = null,
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = dialogTitle,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavyBlue,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                dialogText?.let {
                    Text(
                        text = it,
                        color = DarkGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SimpleGradientButton(
                    text = confirmationButtonText,
                    height = 56.dp,
                    onClickButton = onConfirmation,
                )
                dismissButtonText?.let {
                    SimpleGradientButton(
                        text = it,
                        height = 56.dp,
                        onClickButton = onDismissRequest
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BasicCustomAlertDialogPreview() {
    BasicCustomAlertDialog(
        onDismissRequest = {},
        onConfirmation = {},
        confirmationButtonText = "Confirmar",
        dismissButtonText = "Cancelar",
        dialogTitle = "Título do Alerta",
        dialogText = "Esta é uma mensagem de exemplo para o corpo do alerta customizado."
    )
}

