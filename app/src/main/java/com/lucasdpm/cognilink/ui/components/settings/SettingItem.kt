package com.lucasdpm.cognilink.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.Red
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun SettingItem(
    title: String,
    icon: Int,
    onClick: () -> Unit,
    subtitle: String? = null,
    iconColor: Color = White
) {
    GradientSurface (shape =  RoundedCornerShape(12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    color = White,
                    fontSize = 16.sp
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(16.dp)
            )
        }
    }

}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun SettingItemPreview() {
    CogniLinkTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SettingItem(
                title = "Alterar Senha",
                icon = R.drawable.ic_lock,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingItem(
                title = "Sair",
                subtitle = "Sair da conta",
                icon = R.drawable.ic_cancel,
                onClick = {},
                iconColor = Red
            )
        }
    }
}
