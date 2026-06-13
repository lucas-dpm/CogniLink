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
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicCustomDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    dialogTitle: String,
    dialogText: String? = null,
    buttons: @Composable () -> Unit = {}
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
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
                if (dialogText != null) {
                    Text(
                        text = dialogText,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                buttons()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BasicCustomDialogPreview() {
    BasicCustomDialog(
        dialogTitle = "Confirm Action",
        dialogText = "Are you sure you want to proceed with this operation? This action cannot be undone.",
        buttons = {
            SimpleGradientButton(
                text = "Confirm",
                height = 56.dp,
                onClickButton = { }
            )
            SimpleGradientButton(
                text = "No",
                height = 56.dp,
                onClickButton = { }
            )
            SimpleGradientButton(
                text = "Maybe",
                height = 56.dp,
                onClickButton = { }
            )
        }
    )
}
