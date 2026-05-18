package com.example.cognilink.ui.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import com.example.cognilink.domain.DifficultyLevel
import com.example.cognilink.ui.components.utils.buttons.NeonFAB
import com.example.cognilink.ui.components.input.SearchTextField
import com.example.cognilink.ui.components.home.DeckCard
import com.example.cognilink.ui.components.home.ProfileSection
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.White

@Composable
fun HomeContent(modifier: Modifier = Modifier,
               userName: String = "João Silva",
               welcomePhrase: String = "Pronto para subir de nível no seu conhecimento hoje?",
               searchInput: String = "",
               onSearchValueChange: (String) -> Unit = {}
) {

    var searchValue by remember { mutableStateOf(searchInput) }

    val scrollState = rememberScrollState()

    Scaffold(
        floatingActionButton = {
            NeonFAB(neonColor = White,
                size = 70.dp,
                initialBackgroundColor = DarkNavyBlue,
                finalBackgroundColor = Color(0xFF1222B0) ,
                buttonDescription = "Criar baralho de flashcards",
                iconColor = White,
                icon = R.drawable.ic_add,
                onClick = { /* TODO */ }
            )
        }
    ) { padding ->
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
            ) {

                ProfileSection()

                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Configurações",
                        tint = White
                    )
                }

            }
            Box(modifier = Modifier
                .offset(y = (-18).dp)
            ) {
                NeonFAB(neonColor = White,
                    initialBackgroundColor = DarkNavyBlue,
                    finalBackgroundColor = Color(0xFF1222B0),
                    buttonDescription = "Abrir perfil de usuário",
                    size = 32.dp,
                    iconColor = White,
                    icon = R.drawable.ic_keyboard_arrow_down,
                    onClick = { /* TODO */ },
                )
            }
            Column(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Bem-vindo de volta, $userName!",
                    color = DarkNavyBlue,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Text(text = welcomePhrase,
                    color =  DarkGray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                SearchTextField(modifier = Modifier
                    .fillMaxWidth(),
                    searchValue = searchValue,
                    onSearchValueChange = { searchValue = it }
                )

                Text(text = "SEUS BARALHOS",
                    color =  DarkGray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DeckCard(
                        difficulty = DifficultyLevel.EASY,
                        deckName = "Química Orgânica Básica",
                        category = "Ciência",
                        totalCards = 129,
                        cardsToReview = 34,
                        proficiency = 0.78f
                    )
                    DeckCard(
                        difficulty = DifficultyLevel.MEDIUM,
                        deckName = "Matemática Básica",
                        category = "Matemática",
                        totalCards = 83,
                        cardsToReview = 21,
                        proficiency = 0.99f
                    )
                    DeckCard(
                        difficulty = DifficultyLevel.HARD,
                        deckName = "História Antiga",
                        category = "História",
                        totalCards = 34,
                        cardsToReview = 3,
                        proficiency = 0.34f
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentPreview() {
    CogniLinkTheme {
        HomeContent()
    }
}