package com.example.cognilink.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.cognilink.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.VeryLightGray
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.theme.primaryColor
import com.example.cognilink.ui.theme.secondaryColor
import com.example.cognilink.ui.theme.tertiaryColor


@Composable
fun DeckCard(
    difficulty: DifficultyLevel = DifficultyLevel.HARD,
    deckName: String = "Nome do Baralho",
    category: String = "Ciência",
    totalCards: Int = 120,
    cardsToReview: Int = 10,
    proficiency: Float = 0.85f
) {

    Surface(modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = White
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(color = difficulty.tertiaryColor,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(text = difficulty.toDisplayName(),
                                color = difficulty.primaryColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Text(text = category,
                            color = DarkGray.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()){
                        Text(text = deckName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),color = DarkNavyBlue
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                        Text(text = "$totalCards FlashCards",
                            color = DarkGray,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(painter = painterResource(id = R.drawable.ic_overlay),
                            contentDescription = "ponto",
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                        Text(text = "$cardsToReview para revisar",
                            color = DarkGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Surface(color = difficulty.tertiaryColor,
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_deck),
                        tint = difficulty.primaryColor,
                        modifier = Modifier.padding(10.dp).size(30.dp),
                        contentDescription = null,
                        )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(modifier = Modifier.weight(1f),
                        text = "DOMÍNO",
                        color = DarkGray.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    )
                    val proficiencyPercentage = proficiency * 100
                    Text(text = "$proficiencyPercentage%",
                        color = DarkGray.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()
                ) {
                    if(proficiency > 0f){
                        Surface(modifier = Modifier.weight(proficiency).height(6.dp),
                            color = difficulty.secondaryColor,shape = RoundedCornerShape(9999.dp)
                        ){}
                        Surface(modifier = Modifier.weight(1 - proficiency).height(6.dp),
                            color = VeryLightGray,shape = RoundedCornerShape(topEnd = 9999.dp, bottomEnd = 9999.dp)
                        ){}
                    }
                    else{
                        Surface(modifier = Modifier.weight(1f).height(6.dp),
                            color = VeryLightGray,shape = RoundedCornerShape(topEnd = 9999.dp, bottomEnd = 9999.dp)
                        ){}
                    }
                }
            }
        }

    }
}

@Preview
@Composable
private fun DeckCardPreview() {
    CogniLinkTheme {
        DeckCard()
    }
}