package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.LightGray
import com.lucasdpm.cognilink.ui.theme.MutedBlue
import com.lucasdpm.cognilink.ui.theme.White


@Composable
fun TypeSelector(
    modifier: Modifier = Modifier,
    options: List<FlashcardType>,
    selectedOption: FlashcardType,
    onOptionSelected: (FlashcardType) -> Unit
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(color = MutedBlue,modifier = Modifier.padding(end = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(painter = painterResource(id = selectedOption.getIcon()),
                    contentDescription = null,
                    tint = DarkNavyBlue,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Text(text = selectedOption.getDisplayName(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(painter = painterResource(id = R.drawable.ic_keyboard_arrow_down),
                contentDescription = null
            )

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .border(1.dp,
                        MutedBlue,
                        RoundedCornerShape(12.dp))
                    .fillMaxWidth(0.9f),
                containerColor = White,
                shape = RoundedCornerShape(12.dp)
            ) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = {
                            Text(option.getDisplayName())
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        leadingIcon = {
                            Surface(color = MutedBlue,modifier = Modifier.padding(end = 16.dp),
                                shape = RoundedCornerShape(12.dp)
                            ){
                                Icon(painter = painterResource(id = option.getIcon()),
                                    contentDescription = null,
                                    tint = DarkNavyBlue,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                        }
                    )

                    // Adiciona uma linha divisória entre itens, exceto no último
                    if (index < options.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = LightGray)
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun TypeSelectorPreview() {
    CogniLinkTheme {
        TypeSelector(options = listOf(FlashcardType.BASIC, FlashcardType.MULTIPLE_CHOICE, FlashcardType.OMISSION, FlashcardType.TRUE_OR_FALSE, FlashcardType.CHAT_FEYNMAN),
            selectedOption = FlashcardType.BASIC,
            onOptionSelected = {})
    }
}