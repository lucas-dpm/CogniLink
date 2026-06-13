package com.lucasdpm.cognilink.ui.components.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.Gray
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.theme.White
import com.lucasdpm.cognilink.ui.theme.dashedBorder

@Composable
fun FileUploadArea(modifier: Modifier = Modifier,
                   onUploadClick: () -> Unit = {}
) {
    Column(horizontalAlignment = CenterHorizontally,

        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Surface(color = OffWhite,
            modifier = modifier
                .dashedBorder(
                    color = Gray.copy(alpha = 0.5f),
                    strokeWidth = 2.dp,
                    dashWidth = 10.dp,
                    gapWidth = 5.dp,
                    cornerRadius = 12.dp
                )
                .fillMaxWidth()
                .clickable { onUploadClick() },
            shape = RoundedCornerShape(12.dp),
            ) {
            Column(modifier = Modifier.padding(24.dp),
                horizontalAlignment = CenterHorizontally
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_upload),
                    contentDescription = null,
                    tint = Gray
                )
                Text(text = "Clique para enviar", color = Gray)

                Row(modifier= Modifier.padding(top= 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(shape = RoundedCornerShape(24.dp),color = White) {
                        Text(text = "PDF", modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                        )
                    }
                    Surface(shape = RoundedCornerShape(24.dp),color = White) {
                        Text(text = "Slides", modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FileUploadAreaPreview() {
    CogniLinkTheme {
        FileUploadArea()
    }
}