package com.lucasdpm.cognilink.ui.components.flashcard

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.Gray
import com.lucasdpm.cognilink.ui.theme.White

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
        var quantity by remember { mutableIntStateOf(1) }

        QuantitySelector(quantity = quantity, onQuantityChange = { quantity = it  })
    }
}