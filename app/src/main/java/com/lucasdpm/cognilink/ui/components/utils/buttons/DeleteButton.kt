package com.lucasdpm.cognilink.ui.components.utils.buttons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                tint = Color.White
            )
        },
        size = size.dp,
        neonColor = Red,
        initialBackgroundColor = OffWhite,
        finalBackgroundColor = OffWhite,
        onClick = onClick,
    )
}