package com.lucasdpm.cognilink.ui.components.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun NavigationHeader(
    modifier: Modifier = Modifier,
    title: String = "TOP BAR",
    onBackClick: () -> Unit = {},
    menuEnabled: Boolean = false,
    onMenuClick: () -> Unit = {},
    showMenu: Boolean = false,
    onDismissMenu: () -> Unit = {},
    menuContent: @Composable ColumnScope.() -> Unit = {}
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = White
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp),
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

            Text(
                text = title,
                color = DarkNavyBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if(menuEnabled)
                Box {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "Menu",
                            tint = DarkNavyBlue,
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = onDismissMenu,
                        containerColor = White,
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        menuContent()
                    }
                }


        }
    }
}

@Preview
@Composable
private fun NavigationHeaderPreview() {
    CogniLinkTheme {
        NavigationHeader()
    }
}