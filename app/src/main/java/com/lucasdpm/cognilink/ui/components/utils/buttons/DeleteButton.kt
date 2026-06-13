package com.lucasdpm.cognilink.ui.components.utils.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.theme.Red

@Composable
fun DeleteButton(
    size: Int = 32,
    onClick: () -> Unit,
) {
    NeonFAB(
        icon = R.drawable.ic_delete,
        iconColor = Red,
        buttonDescription = "Deletar",
        size = size.dp,
        neonColor = Red,
        initialBackgroundColor = OffWhite,
        finalBackgroundColor = OffWhite,
        onClick = onClick,
    )
}