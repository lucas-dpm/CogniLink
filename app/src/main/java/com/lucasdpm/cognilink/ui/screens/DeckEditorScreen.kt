package com.lucasdpm.cognilink.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.data.model.FlashcardWithStats
import com.lucasdpm.cognilink.data.preview.PreviewDataProvider
import com.lucasdpm.cognilink.ui.components.deck.EditDeckContent
import com.lucasdpm.cognilink.ui.components.deck.FlashcardItem
import com.lucasdpm.cognilink.ui.components.deck.ShimmerEditDeckContent
import com.lucasdpm.cognilink.ui.components.deck.ShimmerFlashcardItem
import com.lucasdpm.cognilink.ui.components.input.CustomTextField
import com.lucasdpm.cognilink.ui.components.input.SearchTextField
import com.lucasdpm.cognilink.ui.components.utils.EmptyContent
import com.lucasdpm.cognilink.ui.components.utils.FullScreenLoading
import com.lucasdpm.cognilink.ui.components.utils.NavigationHeader
import com.lucasdpm.cognilink.ui.components.utils.buttons.DeleteButton
import com.lucasdpm.cognilink.ui.components.utils.buttons.NeonActionButton
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.components.utils.dialogs.BasicCustomAlertDialog
import com.lucasdpm.cognilink.ui.components.utils.dialogs.BasicCustomDialog
import com.lucasdpm.cognilink.ui.components.utils.labels.CustomLabel
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.LightGray
import com.lucasdpm.cognilink.ui.theme.Red
import com.lucasdpm.cognilink.ui.theme.White
import com.lucasdpm.cognilink.ui.viewmodels.DeckFormViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun DeckEditorScreen(
    userId: String,
    deckId: String?,
    onNavigateBack: () -> Unit,
    onNavigateToCreateFlashcard: (String?) -> Unit,
    onNavigateToEditFlashcard: (String?, String) -> Unit,
    onNavigateToCreateWithIA: (String?) -> Unit,
    viewModel: DeckFormViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId, deckId) {
        viewModel.initialize(deckId, userId)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }

    BackHandler {
        if (uiState.wasEdited) {
            viewModel.toggleChangeDialog()
        } else {
            onNavigateBack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.showCategoryDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.closeCategoryDialog() },
                title = {
                    Text(
                        text = if (uiState.categoryBeingEdited == null) "Nova Categoria" else "Editar Categoria",
                        fontWeight = FontWeight.Bold,
                        color = DarkNavyBlue
                    )
                },
                text = {
                    CustomTextField(
                        inputValue = uiState.categoryText,
                        onInputValueChange = { viewModel.onCategoryTextChange(it) },
                        placeholder = "Ex: Medicina, História..."
                    )
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.handleCategoryConfirmation() }) {
                        Text("OK", fontWeight = FontWeight.Bold, color = DarkNavyBlue)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.closeCategoryDialog() }) {
                        Text("CANCELAR", color = DarkGray)
                    }
                },
                containerColor = White,
                shape = RoundedCornerShape(28.dp)
            )
        }

        if (uiState.showAddFlashcardDialog) {
            BasicCustomDialog(
                onDismissRequest = { viewModel.toggleAddFlashcardDialog() },
                dialogTitle = "Como deseja criar?",
                buttons = {
                    SimpleGradientButton(
                        text = "Criar manualmente",
                        height = 56.dp,
                        onClickButton = {
                            viewModel.toggleAddFlashcardDialog()
                            scope.launch {
                                delay(100)
                                onNavigateToCreateFlashcard(uiState.deckId)
                            }
                        }
                    )
                    SimpleGradientButton(
                        text = "Criar com IA",
                        height = 56.dp,
                        onClickButton = {
                            viewModel.toggleAddFlashcardDialog()
                            scope.launch {
                                delay(100)
                                onNavigateToCreateWithIA(uiState.deckId)
                            }
                        }
                    )
                }
            )
        }

        if (uiState.showChangeDialog) {
            BasicCustomAlertDialog(
                onDismissRequest = { viewModel.toggleChangeDialog() },
                onConfirmation = {
                    viewModel.discardDeck()
                    viewModel.toggleChangeDialog()
                    scope.launch {
                        delay(100)
                        onNavigateBack()
                    }
                },
                dialogTitle = "Alterações não salvas",
                dialogText = "Você possui alterações não salvas. Deseja realmente sair e descartá-las?",
                confirmationButtonText = "Sair e descartar",
                dismissButtonText = "Continuar editando",
                icon = R.drawable.ic_warning,
                iconColor = Red
            )
        }

        if (uiState.showCriticalErrorDialog) {
            BasicCustomAlertDialog(
                onDismissRequest = {
                    scope.launch {
                        delay(150)
                        onNavigateBack()
                    }
                },
                onConfirmation = {
                    scope.launch {
                        delay(150)
                        onNavigateBack()
                    }
                },
                dialogTitle = "Ocorreu um erro!",
                dialogText = uiState.errorMessage ?: "Não foi possível carregar as informações.",
                confirmationButtonText = "Voltar",
                dismissButtonText = null,
                icon = R.drawable.ic_warning,
                iconColor = Red
            )
        }

        DeckEditorContent(
            isEditMode = uiState.isEditMode,
            deckName = uiState.deckName,
            deckNameError = uiState.deckNameError,
            deckDescription = uiState.deckDescription,
            deckCategories = uiState.deckCategories,
            deckFlashcards = uiState.filteredFlashcards,
            isDeckEmpty = uiState.deckFlashcards.isEmpty(),
            searchInput = uiState.searchInput,
            onSearchValueChange = viewModel::onSearchValueChange,
            isRemoveMode = uiState.isRemoveMode,
            onToggleRemoveMode = viewModel::toggleRemoveMode,
            onDeckNameChange = viewModel::onDeckNameChange,
            onDeckDescriptionChange = viewModel::onDeckDescriptionChange,
            onAddCategory = viewModel::openCategoryDialog,
            onEditCategory = { viewModel.openCategoryDialog(it) },
            onRemoveCategory = viewModel::removeCategory,
            onRemoveFlashcard = viewModel::removeFlashcard,
            onAddFlashcard = { viewModel.toggleAddFlashcardDialog() },
            onEditFlashcard = { fId -> onNavigateToEditFlashcard(uiState.deckId, fId) },
            onSave = { viewModel.saveDeck() },
            onNavigateBack = {
                if (uiState.wasEdited) {
                    viewModel.toggleChangeDialog()
                } else {
                    onNavigateBack()
                }
            },
            isLoading = uiState.isLoading
        )

        if (uiState.isSaving) {
            FullScreenLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckEditorContent(
    isEditMode: Boolean,
    deckName: String,
    onDeckNameChange: (String) -> Unit = {},
    deckNameError: String? = null,
    deckDescription: String,
    onDeckDescriptionChange: (String) -> Unit = {},
    deckCategories: List<String>,
    searchInput: String = "",
    onSearchValueChange: (String) -> Unit = {},
    onAddCategory: () -> Unit = {},
    onEditCategory: (String) -> Unit = {},
    deckFlashcards: List<FlashcardWithStats>,
    isDeckEmpty: Boolean = true,
    isRemoveMode: Boolean,
    onRemoveCategory: (String) -> Unit = {},
    onRemoveFlashcard: (String) -> Unit = {},
    onToggleRemoveMode: () -> Unit = {},
    onSave: () -> Unit = {},
    onEditFlashcard: (String) -> Unit = {},
    onAddFlashcard: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    isLoading: Boolean = false,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        containerColor = Color.Transparent,
        topBar = {
            NavigationHeader(
                title = if (isEditMode) "Editar Baralho" else "Novo Baralho",
                onBackClick = onNavigateBack,
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                SimpleGradientButton(
                    text = if (isEditMode) "SALVAR" else "CRIAR",
                    height = 40.dp,
                    icon = R.drawable.ic_check,
                    onClickButton = onSave
                )
            }
        }) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Crossfade(
                    targetState = isLoading,
                    label = "deck_form_shimmer"
                ) { loading ->
                    if (loading) {
                        ShimmerEditDeckContent()
                    } else {
                        EditDeckContent(
                            name = deckName,
                            onNameChange = onDeckNameChange,
                            nameError = deckNameError,
                            categories = deckCategories,
                            onCategoryClickRemove = onRemoveCategory,
                            onCategoryClickAdd = onAddCategory,
                            onCategoryClickEdit = onEditCategory,
                            description = deckDescription,
                            onDescriptionChange = onDeckDescriptionChange
                        )
                    }
                }

                NeonActionButton(
                    text = "ADICIONAR FLASHCARD",
                    icon = R.drawable.ic_add,
                    onClickButton = onAddFlashcard,
                    modifier = Modifier.fillMaxWidth()
                )

                if (!isDeckEmpty) {

                    SearchTextField(
                        searchValue = searchInput,
                        onSearchValueChange = onSearchValueChange,
                        hintText = "Pesquisar flashcards..."
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        CustomLabel(
                            text = "Conteúdo do Baralho",
                            textColor = DarkGray
                        )
                        Text(
                            text = if (isRemoveMode) "VOLTAR PARA SELEÇÃO" else "GERENCIAR",
                            color = DarkNavyBlue,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { onToggleRemoveMode() }
                            )
                        )
                    }

                    if (deckFlashcards.isNotEmpty()) {
                        Column {
                            Crossfade(
                                targetState = isLoading,
                                label = "flashcards_shimmer"
                            ) { loading ->
                                if (loading) {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        repeat(5) {
                                            ShimmerFlashcardItem()
                                        }
                                    }
                                } else {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        deckFlashcards.forEach { cardWithStats ->
                                            val card = cardWithStats.flashcard
                                            FlashcardItem(
                                                flashcardType = card.cardType,
                                                flashcardQuestion = card.question,
                                                nextReview = null,
                                                onSelectCard = {
                                                    if (!isRemoveMode) {
                                                        onEditFlashcard(card.id)
                                                    }
                                                },
                                                selectionControl = {
                                                    if (isRemoveMode) {
                                                        DeleteButton(onClick = {
                                                            onRemoveFlashcard(
                                                                card.id
                                                            )
                                                        })
                                                    } else {
                                                        IconButton(
                                                            onClick = { onEditFlashcard(card.id) },
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
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    } else if (!isLoading) {
                        EmptyContent(
                            title = stringResource(R.string.deck_search_no_results_title),
                            subTitle = stringResource(R.string.deck_search_no_results_subtitle)
                        )
                    }
                }  else {
                    if (!isLoading) {
                        EmptyContent()
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeckEditorContentPreview() {
    CogniLinkTheme {
        val deck = PreviewDataProvider.deck
        val flashcards = PreviewDataProvider.flashcardList.map { FlashcardWithStats(it, null) }

        DeckEditorContent(
            isEditMode = true,
            deckName = deck.name,
            deckDescription = deck.description,
            deckCategories = deck.categories,
            deckFlashcards = flashcards,
            isDeckEmpty = flashcards.isEmpty(),
            isRemoveMode = false,
            isLoading = false,
        )

    }
}
