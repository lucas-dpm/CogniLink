package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun HintReveal(
    modifier: Modifier = Modifier,
    hints: List<String> = emptyList(),
) {
    var hintsVisibleCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        hints.take(hintsVisibleCount).forEachIndexed { index, hint ->
            val index = index + 1
            HintItem(
                label = "DICA $index ",
                hint = hint,
            )
        }

        if (hintsVisibleCount < hints.size) {
            GradientSurface(
                shape = RoundedCornerShape(50),
                shadowElevation = 1.dp
            ) {
                TextButton(
                    onClick = { hintsVisibleCount++ },
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_lightbulb),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("VER DICA (${hintsVisibleCount + 1}/${hints.size})", color = White)
                }
            }

        } else if(hintsVisibleCount == hints.size && hints.isNotEmpty()){
            Text(
                text = "Todas as dicas reveladas",
                fontSize = 12.sp,
                color = DarkGray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun HintRevealPreview() {
    CogniLinkTheme {
        HintReveal(hints = listOf("Dica 1", "Dica 2", "Dica 3"))
    }
}