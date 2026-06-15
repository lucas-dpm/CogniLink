package com.lucasdpm.cognilink.ui.components.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.states.CustomSnackbarVisuals
import com.lucasdpm.cognilink.ui.states.SnackbarType
import com.lucasdpm.cognilink.ui.theme.Black
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.Gray
import com.lucasdpm.cognilink.ui.theme.Red
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    containerColor: Color = White,
    contentColor: Color = Black
) {
    val customVisuals = snackbarData.visuals as? CustomSnackbarVisuals
    val icon = customVisuals?.icon ?: R.drawable.ic_warning
    val iconColor = customVisuals?.iconColor ?: Red

    Surface(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 6.dp,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, iconColor.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = snackbarData.visuals.message,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                snackbarData.visuals.actionLabel?.let { label ->
                    TextButton(
                        onClick = { snackbarData.performAction() },
                        colors = ButtonDefaults.textButtonColors(contentColor = iconColor)
                    ) {
                        Text(text = label)
                    }
                }
                
                if (snackbarData.visuals.withDismissAction) {
                    IconButton(
                        onClick = { snackbarData.dismiss() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Fechar",
                            tint = Gray
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomSnackbarPreview() {
    CogniLinkTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomSnackbar(
                snackbarData = object : SnackbarData {
                    override val visuals = CustomSnackbarVisuals(
                        message = "Erro ao carregar dados",
                        type = SnackbarType.ERROR,
                        withDismissAction = true
                    )
                    override fun dismiss() {}
                    override fun performAction() {}
                }
            )
            CustomSnackbar(
                snackbarData = object : SnackbarData {
                    override val visuals = CustomSnackbarVisuals(
                        message = "Item salvo com sucesso!",
                        type = SnackbarType.SUCCESS,
                        withDismissAction = false
                    )
                    override fun dismiss() {}
                    override fun performAction() {}
                }
            )
        }
    }
}
