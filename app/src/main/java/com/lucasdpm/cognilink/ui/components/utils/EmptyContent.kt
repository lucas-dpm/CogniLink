package com.lucasdpm.cognilink.ui.components.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.LavenderBlue
import com.lucasdpm.cognilink.ui.theme.VeryLightGray

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.empty_deck_content_default_title),
    subTitle: String = stringResource(R.string.empty_deck_content_default_subtitle),
    icon: Int = R.drawable.ic_deck,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = VeryLightGray.copy(alpha = 0.5f),
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(24.dp).height(300.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = DarkNavyBlue.copy(alpha = 0.6f),
                modifier = Modifier.size(60.dp),
            )
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
            if (subTitle.isNotBlank()) {
                Text(
                    text = subTitle,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = DarkGray,
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = CenterVertically
            ) {
                repeat(3) { i ->
                    Icon(
                        painter = painterResource(id = R.drawable.ic_overlay),
                        contentDescription = null,
                        tint = LavenderBlue.copy(alpha = 1f / (i + 1)),
                        modifier = Modifier
                            .size(8.dp)
                            .padding(horizontal = 2.dp)
                    )
                }
            }
        }
    }
}