package com.example.cognilink.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Gray
import com.example.cognilink.ui.theme.White

@Composable
fun QuantitySelector(
    modifier: Modifier = Modifier,
    quantity: Int, // Agora o componente apenas lê o que o pai manda
    onQuantityChange: (Int) -> Unit
) {
    Surface(
        color = White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            // Botão de Menos
            IconButton(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                enabled = quantity > 1
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_remove),
                    contentDescription = "Diminuir",
                    tint = if (quantity > 1) DarkNavyBlue else Gray,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = quantity.toString(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            // Botão de Mais
            IconButton(
                onClick = { onQuantityChange(quantity + 1) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Aumentar",
                    tint = DarkNavyBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun QuantitySelectorPreview() {
    CogniLinkTheme {
        QuantitySelector(quantity = 1, onQuantityChange = {})
    }
}