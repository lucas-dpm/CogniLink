package com.example.cognilink.ui.screens.deck

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.model.deck1
import com.example.cognilink.ui.components.utils.buttons.NeonActionButton
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.components.deck.FlashcardItem
import com.example.cognilink.ui.components.utils.EmptyContent
import com.example.cognilink.ui.components.deck.ViewDeckContent
import com.example.cognilink.ui.components.utils.labels.CustomLabel
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.viewmodels.DeckViewModel


import androidx.compose.runtime.LaunchedEffect

@Composable
fun DeckScreen(
    deckId: Long,
    userId: Long,
    onBackClick: () -> Unit,
    viewModel: DeckViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(deckId, userId) {
        viewModel.initialize(deckId, userId)
    }

    val deck = uiState.currentDeck
    DeckContent(
        deckName = deck?.name,
        deckCategories = deck?.categories,
        deckDescription = deck?.description,
        deckDifficulty = deck?.difficulty,
        deckMastery = deck?.mastery,
        deckTotalCards = deck?.totalCards,
        deckCardsToReview = deck?.cardsToReview,
        deckFlashcards = uiState.flashcards,
        onMenuClick = { /* TODO: DropDownMenu: Abrir menu de opções(editar,exclui) */ },
        onAddFlashcardClick = { /* TODO: Navegar para criação de card */ },
        onFlashcardClick = { /* TODO: Navegar para jogar de card */ },
        onClickSeeMore = { /* TODO: Navegar para lista de flashcards */ },
        onBackClick = onBackClick
    )
}

@Composable
fun DeckContent(
    modifier: Modifier = Modifier,
    deckName: String?,
    deckCategories: List<String>?,
    deckDifficulty: DifficultyLevel?,
    deckDescription: String?,
    deckMastery: Float?,
    deckTotalCards: Int?,
    deckCardsToReview: Int?,
    deckFlashcards: List<Flashcard>?,
    onAddFlashcardClick: () -> Unit,
    onFlashcardClick: (Flashcard) -> Unit,
    onClickSeeMore: () -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                SimpleGradientButton(
                    text = "ESTUDAR AGORA",
                    height = 40.dp,
                    icon = R.drawable.ic_arrow_forward,
                    iconRightSide = true,
                    onClickButton = {}
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(padding),
        ) {
            NavigationHeader(
                title = deckName ?: "Nome do Baralho",
                onMenuClick = onMenuClick,
                onBackClick = onBackClick,
                menuEnabled = true
            )
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ViewDeckContent(
                    categories = deckCategories,
                    difficulty = deckDifficulty,
                    name = deckName,
                    description = deckDescription,
                    mastery = deckMastery,
                    totalCards = deckTotalCards,
                    cardToReview = deckCardsToReview,
                )

                NeonActionButton(
                    text = "ADICIONAR FLASHCARD",
                    icon = R.drawable.ic_add,
                    onClickButton = onAddFlashcardClick
                )

                if (!deckFlashcards.isNullOrEmpty()) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        CustomLabel("Próximos tópicos", textColor = DarkNavyBlue)
                        TextButton(onClick = onClickSeeMore, contentPadding = PaddingValues(0.dp)) {
                            Text(
                                text = "Ver todos",
                                color = DarkNavyBlue,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        deckFlashcards.forEach { flashcard ->
                            FlashcardItem(flashcardType = flashcard.cardType, flashcardQuestion = flashcard.question, nextReview = "10",onSelectCard = {onFlashcardClick(flashcard)})
                        }
                    }
                }
                else
                    EmptyContent()
            }
        }
    }
}



@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeckContentPreview() {
    CogniLinkTheme {
        DeckContent(
            deckName = deck1.name,
            deckCategories = deck1.categories,
            deckDescription = deck1.description,
            deckDifficulty = deck1.difficulty,
            deckMastery = deck1.mastery,
            deckTotalCards = deck1.totalCards,
            deckCardsToReview = deck1.cardsToReview,
            deckFlashcards = emptyList(),
            onMenuClick = {},
            onAddFlashcardClick = {},
            onFlashcardClick = {},
            onClickSeeMore = {},
            onBackClick = {}
        )
    }
}
