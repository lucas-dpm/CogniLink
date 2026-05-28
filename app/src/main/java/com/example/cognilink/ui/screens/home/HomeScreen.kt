package com.example.cognilink.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.data.model.Deck
import com.example.cognilink.data.repository.DeckRepositoryImpl
import com.example.cognilink.data.repository.UserRepositoryImpl
import com.example.cognilink.ui.components.home.DeckCard
import com.example.cognilink.ui.components.home.ProfileSection
import com.example.cognilink.ui.components.input.SearchTextField
import com.example.cognilink.ui.components.utils.EmptyContent
import com.example.cognilink.ui.components.utils.buttons.NeonFAB
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    userId: Long,
    onNavigateToCreateDeck: () -> Unit = { },
    onNavigateToDeck: (Long) -> Unit = { },
    onNavigateToProfile: () -> Unit = {},
    viewModel: HomeViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(UserRepositoryImpl(), DeckRepositoryImpl()) as T
            }
        }
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.initialize(userId)
    }

    HomeContent(
        userName = uiState.userName,
        welcomePhrase = uiState.welcomePhrase,
        overallMastery = uiState.overallMastery,
        totalStudyTime = viewModel.formatTime(uiState.totalStudyTime),
        cardsDone = uiState.cardsDone,
        learnRetention = uiState.retentionRate,
        searchInput = uiState.searchInput,
        decks = uiState.decks,
        onSearchValueChange = viewModel::onSearchValueChange,
        onDeckClick = onNavigateToDeck,
        onCreateDeckClick = onNavigateToCreateDeck,
        onNavigateToProfile = onNavigateToProfile
    )
}

@Composable
fun HomeContent(
    userName: String = "João Silva",
    welcomePhrase: String = "Pronto para subir de nível no seu conhecimento hoje?",
    overallMastery: Float = 0f,
    totalStudyTime: String = "",
    cardsDone: Int = 0,
    learnRetention: Float = 0f,
    searchInput: String = "",
    decks: List<Deck> = emptyList(),
    onSearchValueChange: (String) -> Unit = {},
    onDeckClick: (Long) -> Unit = { },
    onCreateDeckClick: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {

    val scrollState = rememberScrollState()

    Scaffold(
        floatingActionButton = {
            NeonFAB(
                neonColor = White,
                size = 70.dp,
                initialBackgroundColor = DarkNavyBlue,
                finalBackgroundColor = Color(0xFF1222B0),
                buttonDescription = "Criar baralho de flashcards",
                iconColor = White,
                icon = R.drawable.ic_add,
                onClick = onCreateDeckClick
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ProfileSection(
                    userName = userName,
                    overallMastery = overallMastery,
                    totalStudyTime = totalStudyTime,
                    cardsDone = cardsDone,
                    learnRetention = learnRetention,
                    modifier = Modifier.padding(top = padding.calculateTopPadding())
                )

                IconButton(
                    onClick = { /* TODO: Abrir tela de configurações */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 39.dp, end = 19.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Configurações",
                        tint = White
                    )
                }

            }
            Box(
                modifier = Modifier
                    .offset(y = (-18).dp)
            ) {
                NeonFAB(
                    neonColor = White,
                    initialBackgroundColor = DarkNavyBlue,
                    finalBackgroundColor = Color(0xFF1222B0),
                    buttonDescription = "Abrir perfil de usuário",
                    size = 32.dp,
                    iconColor = White,
                    icon = R.drawable.ic_keyboard_arrow_down,
                    onClick = onNavigateToProfile,
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Bem-vindo de volta, $userName!",
                    color = DarkNavyBlue,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Text(
                    text = welcomePhrase,
                    color = DarkGray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                SearchTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    searchValue = searchInput,
                    onSearchValueChange = onSearchValueChange
                )

                Text(
                    text = "SEUS BARALHOS",
                    color = DarkGray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (decks.isEmpty()) {
                        EmptyContent(
                            title = if (searchInput.isNotEmpty()) stringResource(R.string.home_search_no_results_title) else stringResource(
                                R.string.home_empty_content_title
                            ),
                            subTitle = if (searchInput.isNotEmpty()) stringResource(R.string.home_search_no_results_subtitle) else stringResource(
                                R.string.home_empty_content_subtitle
                            )
                        )
                    } else {
                        decks.forEach { deck ->
                            DeckCard(
                                modifier = Modifier.clickable { onDeckClick(deck.id) },
                                difficulty = deck.difficulty,
                                deckName = deck.name,
                                category = deck.categories.firstOrNull() ?: "Geral",
                                totalCards = deck.totalCards,
                                cardsToReview = deck.cardsToReview,
                                proficiency = deck.mastery
                            )
                        }
                    }
                }
                Box(modifier = Modifier.padding(bottom = 50.dp))
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