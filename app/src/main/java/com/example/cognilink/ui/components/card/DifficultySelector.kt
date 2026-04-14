package com.example.cognilink.ui.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LightGray
import com.example.cognilink.ui.theme.MutedBlue
import com.example.cognilink.ui.theme.White

@Composable
fun DifficultySelector(
    difficultyLevels: List<DifficultyLevel>,
    selectedDifficulty: DifficultyLevel,
    onDifficultySelected: (DifficultyLevel) -> Unit,
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }

    Surface(color = White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = modifier.clickable{
                expanded = !expanded
            }
                .padding(16.dp)
        ) {
            Text(text = selectedDifficulty.toDisplayName(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(painter = painterResource(id = R.drawable.ic_keyboard_arrow_down),
                contentDescription = null
            )

        }
        Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopStart)) {

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(250.dp).border(1.dp, MutedBlue, RoundedCornerShape(12.dp)),
                containerColor = White,
                shape = RoundedCornerShape(12.dp)
            ) {
                difficultyLevels.forEachIndexed { index, difficulty ->
                    DropdownMenuItem(
                        text = {
                            Text(difficulty.toDisplayName())
                        },
                        onClick = {
                            onDifficultySelected(difficulty)
                            expanded = false
                        },
                        leadingIcon = {
                            val icon = if (difficulty == selectedDifficulty) R.drawable.ic_check_circle else R.drawable.ic_circle
                            Icon(painter = painterResource(id = icon), contentDescription = null, tint = DarkNavyBlue)
                        }
                    )

                    // Adiciona uma linha divisória entre itens, exceto no último
                    if (index < difficultyLevels.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = LightGray)
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun DifficultySelectorPreview() {
    CogniLinkTheme {
        DifficultySelector(
            difficultyLevels = listOf(DifficultyLevel.EAZY, DifficultyLevel.MEDIUM,DifficultyLevel.HARD),
            selectedDifficulty = DifficultyLevel.EAZY,
            onDifficultySelected = {}
        )
    }
}