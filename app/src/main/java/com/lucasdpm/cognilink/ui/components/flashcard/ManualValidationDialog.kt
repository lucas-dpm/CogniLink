package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.Green
import com.lucasdpm.cognilink.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualValidationDialog(
    onAnswerResult: (Boolean) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { }, // Não permite fechar sem escolher
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_warning),
                    contentDescription = null,
                    tint = DarkNavyBlue,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Você está Offline!",
                    fontWeight = FontWeight.Bold,
                    color = DarkNavyBlue,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Não foi possível validar sua resposta semanticamente. Compare sua resposta com a correta e nos diga:",
                    color = DarkGray,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SimpleGradientButton(
                        text = "Errei",
                        onClickButton = { onAnswerResult(false) },
                        modifier = Modifier.weight(1f),
                        initialColor = Red,
                        finalColor = Red.copy(alpha = 0.8f)
                    )
                    SimpleGradientButton(
                        text = "Acertei",
                        onClickButton = { onAnswerResult(true) },
                        modifier = Modifier.weight(1f),
                        initialColor = Green,
                        finalColor = Green.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
