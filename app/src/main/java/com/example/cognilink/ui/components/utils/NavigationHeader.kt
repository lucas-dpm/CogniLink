package com.example.cognilink.ui.components.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.White

@Composable
fun NavigationHeader(modifier: Modifier = Modifier,
                     title: String = "TOP BAR",
                     onBackClick: () -> Unit = {},
                     menuEnabled: Boolean = false,
                     onMenuClick: () -> Unit = {},
) {

    Surface(
            modifier = Modifier
                .fillMaxWidth(),
        color = White
        ) {
            Row(
                modifier = modifier.fillMaxWidth().height(64.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Voltar",
                    tint = DarkNavyBlue,
                    modifier = Modifier
                )
            }

            Text(text = title,
                color = DarkNavyBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f))

                IconButton(onClick = onMenuClick, enabled = menuEnabled) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Menu",
                        tint = if (menuEnabled) DarkNavyBlue else Transparent,
                    )
                }


        }
    }
}

@Preview
@Composable
private fun NavigationHeaderPreview() {
    CogniLinkTheme{
        NavigationHeader()
    }
}