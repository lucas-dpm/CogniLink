package com.lucasdpm.cognilink.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.data.model.Deck
import com.lucasdpm.cognilink.data.preview.PreviewDataProvider
import com.lucasdpm.cognilink.ui.components.home.DeckCard
import com.lucasdpm.cognilink.ui.components.home.ProfileSection
import com.lucasdpm.cognilink.ui.components.input.SearchTextField
import com.lucasdpm.cognilink.ui.components.utils.EmptyContent
import com.lucasdpm.cognilink.ui.components.utils.buttons.NeonActionButton
import com.lucasdpm.cognilink.ui.components.utils.buttons.NeonFAB
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.White
import com.lucasdpm.cognilink.ui.viewmodels.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    userId: String,
    onNavigateToCreateDeck: (String) -> Unit = {},
    onNavigateToDeck: (String) -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToPlay: (String) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.initialize(userId)
    }

    HomeContent(
        userName = uiState.userName,
        homeInsight = uiState.welcomePhrase,
        overallMastery = uiState.overallMastery,
        totalStudyTime = viewModel.formatTime(uiState.totalStudyTime),
        cardsDone = uiState.cardsDone,
        learnRetention = uiState.retentionRate,
        searchInput = uiState.searchInput,
        decks = uiState.decks,
        onSearchValueChange = viewModel::onSearchValueChange,
        onDeckClick = onNavigateToDeck,
        onNavigateToCreateDeck = { onNavigateToCreateDeck(userId) },
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToPlay = { onNavigateToPlay(userId) }
    )
}

@Composable
fun HomeContent(
    userName: String,
    homeInsight: String,
    overallMastery: Float,
    totalStudyTime: String,
    cardsDone: Int,
    learnRetention: Float,
    searchInput: String = "",
    decks: List<Deck> = emptyList(),
    onSearchValueChange: (String) -> Unit = {},
    onDeckClick: (String) -> Unit = { },
    onNavigateToCreateDeck: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToPlay: () -> Unit = {}
) {

    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                NeonActionButton(
                    text = "REVISAR AGORA",
                    icon = R.drawable.ic_cached,
                    onClickButton = onNavigateToPlay,
                    modifier = Modifier.weight(1f)
                )
                NeonFAB(
                    neonColor = White,
                    size = 70.dp,
                    initialBackgroundColor = DarkNavyBlue,
                    finalBackgroundColor = Color(0xFF1222B0),
                    buttonDescription = "Criar baralho de flashcards",
                    iconColor = White,
                    icon = R.drawable.ic_add,
                    onClick = onNavigateToCreateDeck
                )
            }

        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(24.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ProfileSection(
                    userName = userName,
                    homeInsight = homeInsight,
                    overallMastery = overallMastery,
                    totalStudyTime = totalStudyTime,
                    cardsDone = cardsDone,
                    learnRetention = learnRetention,
                    onOpenProfileClick = onNavigateToProfile,
                    modifier = Modifier.padding(top = padding.calculateTopPadding() + 20.dp)
                )

                IconButton(
                    onClick = { /* TODO: Abrir tela de configurações */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = padding.calculateTopPadding() + 25.dp, end = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Configurações",
                        tint = White
                    )
                }

            }

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
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (decks.isEmpty()) {
                    EmptyContent(
                        title = if (searchInput.isNotEmpty()) stringResource(R.string.home_search_no_results_title) else stringResource(
                            R.string.home_empty_content_title
                        ),
                        subTitle = if (searchInput.isNotEmpty()) stringResource(R.string.home_search_no_results_subtitle) else stringResource(
                            R.string.home_empty_content_subtitle
                        ),
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
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentPreview() {
    CogniLinkTheme {
        val user = PreviewDataProvider.user
        val decks = PreviewDataProvider.deckList
        val userStats = PreviewDataProvider.userStats
        HomeContent(
            userName = user.name,
            homeInsight = "Pronto para subir de nível?",
            overallMastery = userStats.overallMastery,
            totalStudyTime = "20d",
            cardsDone = userStats.totalFlashcardsDone,
            learnRetention = userStats.retentionRate,
            decks = decks
        )
    }
}
