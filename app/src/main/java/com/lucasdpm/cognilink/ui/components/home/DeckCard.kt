package com.lucasdpm.cognilink.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.lucasdpm.cognilink.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.ui.components.utils.ProgressBar
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.theme.White
import com.lucasdpm.cognilink.ui.theme.primaryColor
import com.lucasdpm.cognilink.ui.theme.secondaryColor
import com.lucasdpm.cognilink.ui.theme.tertiaryColor


@Composable
fun DeckCard(
    modifier: Modifier = Modifier,
    difficulty: DifficultyLevel = DifficultyLevel.HARD,
    deckName: String = "Nome do Baralho",
    category: String = "Ciência",
    totalCards: Int = 120,
    cardsToReview: Int = 10,
    proficiency: Float = 0.85f
) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = White,
        shadowElevation = 2.dp
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
                            ),color = DarkNavyBlue,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
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

                    Text(text = "${(proficiency * 100).toInt()}%",
                        color = DarkGray.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                ProgressBar(
                    progress = proficiency,
                    progressColor = difficulty.secondaryColor,
                    backgroundColor = OffWhite,
                )
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