package com.lucasdpm.cognilink.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.data.model.FlashcardWithStats
import com.lucasdpm.cognilink.data.preview.PreviewDataProvider
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.ui.components.deck.FlashcardItem
import com.lucasdpm.cognilink.ui.components.deck.ViewDeckContent
import com.lucasdpm.cognilink.ui.components.utils.dialogs.BasicCustomAlertDialog
import com.lucasdpm.cognilink.ui.components.utils.EmptyContent
import com.lucasdpm.cognilink.ui.components.utils.NavigationHeader
import com.lucasdpm.cognilink.ui.components.utils.buttons.NeonActionButton
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.components.utils.dialogs.BasicCustomDialog
import com.lucasdpm.cognilink.ui.components.utils.labels.CustomLabel
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.LightGray
import com.lucasdpm.cognilink.ui.viewmodels.DeckViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeckScreen(
    deckId: String,
    userId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onNavigateToCreateFlashcard: (String) -> Unit,
    onNavigateToCreateWithIA: (String) -> Unit,
    onNavigateToStudy: (String) -> Unit,
    onNavigateToFlashcard: (String) -> Unit,
    viewModel: DeckViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()


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
        isMenuExpanded = uiState.isMenuExpanded,
        isAddFlashcardDialogOpen = uiState.isAddFlashcardDialogOpen,
        isDeleteDeckDialogOpen = uiState.isDeleteDeckDialogOpen,
        onMenuClick = { viewModel.toggleMenu() },
        onClickAddFlashcardDialog = { viewModel.toggleAddFlashcardDialog() },
        onDismissAddFlashcardDialog = { viewModel.toggleAddFlashcardDialog() },
        onCreateFlashcardManually = {
            viewModel.toggleAddFlashcardDialog()
            scope.launch {
                delay(100)
                onNavigateToCreateFlashcard(deckId)
            }
        },
        onCreateFlashcardWithIA = {
            viewModel.toggleAddFlashcardDialog()
            scope.launch {
                delay(100)
                onNavigateToCreateWithIA(deckId)
            }
        },
        onFlashcardClick = { flashcardId -> onNavigateToFlashcard(flashcardId) },
        onStudyNowClick = {
            onNavigateToStudy(deckId)
        },
        onClickSeeMore = { viewModel.loadAllFlashcards() },
        onBackClick = onNavigateBack,
        onConfirmDelete = {
            viewModel.toggleDeleteDeckDialog()
            viewModel.deleteDeck()
            scope.launch {
                delay(100)
                onNavigateBack()
            }
        },
        onEditClick = {
            viewModel.toggleMenu()
            scope.launch {
                delay(100)
                onNavigateToEdit()
            }
        },
        onClickDeleteDeckDialog = {
            viewModel.toggleMenu()
            viewModel.toggleDeleteDeckDialog()
        },
        onDismissDeleteDeckDialog = {
            viewModel.toggleDeleteDeckDialog()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckContent(
    deckName: String?,
    deckCategories: List<String>?,
    deckDifficulty: DifficultyLevel?,
    deckDescription: String?,
    deckMastery: Float?,
    deckTotalCards: Int?,
    deckCardsToReview: Int?,
    deckFlashcards: List<FlashcardWithStats>?,
    isMenuExpanded: Boolean = false,
    isAddFlashcardDialogOpen: Boolean = false,
    onCreateFlashcardWithIA: () -> Unit = {},
    onCreateFlashcardManually: () -> Unit = {},
    onFlashcardClick: (String) -> Unit = {},
    onStudyNowClick: () -> Unit = {},
    onClickSeeMore: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onConfirmDelete: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onClickAddFlashcardDialog: () -> Unit = {},
    onDismissAddFlashcardDialog: () -> Unit = {},
    onClickDeleteDeckDialog: () -> Unit = {},
    onDismissDeleteDeckDialog: () -> Unit = {},
    isDeleteDeckDialogOpen: Boolean = false,
) {
    val scrollState = rememberScrollState()

    if (isDeleteDeckDialogOpen) {
        BasicCustomAlertDialog(
            dialogTitle = "Tem certeza disso?",
            dialogText = "Essa ação não poderá ser desfeita!",
            confirmationButtonText = "Sim",
            dismissButtonText = "Cancelar",
            onConfirmation = onConfirmDelete,
            onDismissRequest = onDismissDeleteDeckDialog,
        )
    }

    if (isAddFlashcardDialogOpen) {
        BasicCustomDialog(
            onDismissRequest = onDismissAddFlashcardDialog,
            dialogTitle = "Como deseja criar?",
            buttons = {
                SimpleGradientButton(
                    text = "Criar manualmente",
                    height = 56.dp,
                    onClickButton = {
                        onCreateFlashcardManually()
                    }
                )
                SimpleGradientButton(
                    text = "Criar com IA",
                    height = 56.dp,
                    onClickButton = {
                        onCreateFlashcardWithIA()
                    }
                )
            }
        )
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        containerColor = Color.Transparent,
        topBar = {
            NavigationHeader(
                title = deckName ?: "Nome do Baralho",
                onMenuClick = onMenuClick,
                onBackClick = onBackClick,
                menuEnabled = true,
                showMenu = isMenuExpanded,
                onDismissMenu = onMenuClick,
                menuContent = {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            onEditClick()
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = LightGray
                    )
                    DropdownMenuItem(
                        text = { Text("Excluir") },
                        onClick = {
                            onClickDeleteDeckDialog()
                        }
                    )
                },
            )
        },
        bottomBar = {
            if (!deckFlashcards.isNullOrEmpty()) {
                Column(modifier = Modifier.padding(24.dp)) {
                    SimpleGradientButton(
                        text = "ESTUDAR AGORA",
                        height = 40.dp,
                        icon = R.drawable.ic_arrow_forward,
                        iconRightSide = true,
                        onClickButton = onStudyNowClick
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(padding),
        ) {
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
                    onClickButton = onClickAddFlashcardDialog,
                    modifier = Modifier.fillMaxWidth()
                )

                if (!deckFlashcards.isNullOrEmpty()) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        CustomLabel(text = "Próximos tópicos", textColor = DarkNavyBlue)
                        TextButton(onClick = onClickSeeMore, contentPadding = PaddingValues(0.dp)) {
                            Text(
                                text = "Ver todos",
                                color = DarkNavyBlue,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        deckFlashcards.forEach { flashcardWithStats ->
                            val flashcard = flashcardWithStats.flashcard
                            val stats = flashcardWithStats.stats
                            
                            FlashcardItem(
                                flashcardType = flashcard.cardType,
                                flashcardQuestion = flashcard.question,
                                nextReview = stats?.nextReview?.let { "Revisar em $it" } ?: "Novo card",
                                onSelectCard = { onFlashcardClick(flashcard.id) },
                                selectionControl = {
                                    IconButton(
                                        onClick = { onFlashcardClick(flashcard.id) },
                                        modifier = Modifier
                                            .offset(x = 10.dp)
                                            .size(32.dp)
                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.ic_keyboard_arrow_down),
                                            contentDescription = null,
                                            tint = LightGray,
                                            modifier = Modifier.rotate(-90f)
                                        )
                                    }
                                }
                            )
                        }
                    }
                } else
                    EmptyContent()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeckContentPreview() {
    CogniLinkTheme {
        val deck = PreviewDataProvider.deck
        val flashcards = PreviewDataProvider.flashcardList
            .filter { it.deckId == deck.id }
            .map { FlashcardWithStats(it, null) }

        DeckContent(
            deckName = deck.name,
            deckCategories = deck.categories,
            deckDescription = deck.description,
            deckDifficulty = deck.difficulty,
            deckMastery = deck.mastery,
            deckTotalCards = deck.totalCards,
            deckCardsToReview = deck.cardsToReview,
            deckFlashcards = flashcards,
        )
    }
}
