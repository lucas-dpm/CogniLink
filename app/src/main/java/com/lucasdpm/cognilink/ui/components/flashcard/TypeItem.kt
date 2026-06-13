package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.MutedBlue
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun TypeItem(
    modifier: Modifier = Modifier,
    typeName: String,
    typeDefinition: String,
    icon: Int,
    checked: Boolean, // Estado vem de fora
    onSelect: () -> Unit // Notifica a seleção
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        onClick = onSelect,
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 2.dp,
        color = White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MutedBlue,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(30.dp),
                    tint = DarkNavyBlue
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = typeName, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(text = typeDefinition, fontSize = 11.sp, color = Color.DarkGray, lineHeight = 14.sp)
            }

            RadioButton(
                selected = checked,
                onClick = onSelect, // Sincronizado com o clique no Surface
                colors = RadioButtonDefaults.colors(selectedColor = DarkNavyBlue)
            )
        }
    }
}

@Preview
@Composable
private fun TypeItemPreview() {
    CogniLinkTheme {
        TypeItem(
            typeName = "Pergunta e Resposta",
            typeDefinition = "Ideal para fatos diretos",
            icon = R.drawable.ic_basic_card,
            checked = true,
            onSelect = { }
        )
    }
}