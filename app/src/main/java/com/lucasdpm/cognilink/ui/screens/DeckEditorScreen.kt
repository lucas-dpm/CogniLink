package com.lucasdpm.cognilink.ui.screens

import androidx.activity.compose.BackHandler
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
import com.lucasdpm.cognilink.ui.components.deck.EditDeckContent
import com.lucasdpm.cognilink.ui.components.deck.FlashcardItem
import com.lucasdpm.cognilink.ui.components.input.CustomTextField
import com.lucasdpm.cognilink.ui.components.utils.dialogs.BasicCustomAlertDialog
import com.lucasdpm.cognilink.ui.components.utils.EmptyContent
import com.lucasdpm.cognilink.ui.components.utils.NavigationHeader
import com.lucasdpm.cognilink.ui.components.utils.buttons.DeleteButton
import com.lucasdpm.cognilink.ui.components.utils.buttons.NeonActionButton
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.components.utils.dialogs.BasicCustomDialog
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.LightGray
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

    DeckEditorContent(
        isEditMode = uiState.isEditMode,
        deckName = uiState.deckName,
        deckNameError = uiState.deckNameError,
        deckDescription = uiState.deckDescription,
        deckCategories = uiState.deckCategories,
        deckFlashcards = uiState.deckFlashcards,
        showCategoryDialog = uiState.showCategoryDialog,
        categoryBeingEdited = uiState.categoryBeingEdited,
        categoryText = uiState.categoryText,
        isRemoveMode = uiState.isRemoveMode,
        showAddFlashcardDialog = uiState.showAddFlashcardDialog,
        showChangeDialog = uiState.showChangeDialog,
        onDismissChangeDialog = viewModel::toggleChangeDialog,
        onToggleRemoveMode = viewModel::toggleRemoveMode,
        onDeckNameChange = viewModel::onDeckNameChange,
        onDeckDescriptionChange = viewModel::onDeckDescriptionChange,
        onCategoryTextChange = viewModel::onCategoryTextChange,
        onAddCategory = { viewModel.openCategoryDialog() },
        onEditCategory = { viewModel.openCategoryDialog(it) },
        onRemoveCategory = viewModel::removeCategory,
        onConfirmCategory = viewModel::handleCategoryConfirmation,
        onRemoveFlashcard = viewModel::removeFlashcard,
        onDismissCategoryDialog = viewModel::closeCategoryDialog,
        onAddFlashcard = { viewModel.toggleAddFlashcardDialog() },
        onEditFlashcard = { fId -> onNavigateToEditFlashcard(uiState.deckId, fId) },
        onDismissAddFlashcardDialog = { viewModel.toggleAddFlashcardDialog() },
        onConfirmDiscard = {
            viewModel.discardDeck()
            viewModel.toggleChangeDialog()
            scope.launch {
                delay(100)
                onNavigateBack()
            }
        },
        onCreateFlashcardManually = {
            viewModel.toggleAddFlashcardDialog()
            scope.launch {
                delay(100)
                onNavigateToCreateFlashcard(uiState.deckId)
            }

        },
        onCreateFlashcardWithIA = {
            viewModel.toggleAddFlashcardDialog()
            scope.launch {
                delay(100)
                onNavigateToCreateWithIA(uiState.deckId)
            }
        },
        onSave = {
            viewModel.saveDeck()
        },
        onNavigateBack = {
            if (uiState.wasEdited) {
                viewModel.toggleChangeDialog()
            } else {
                onNavigateBack()
            }
        }
    )
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
    onCategoryTextChange: (String) -> Unit = {},
    onAddCategory: () -> Unit = {},
    onEditCategory: (String) -> Unit = {},
    categoryBeingEdited: String?,
    categoryText: String,
    deckFlashcards: List<FlashcardWithStats>,
    showCategoryDialog: Boolean,
    showAddFlashcardDialog: Boolean,
    showChangeDialog: Boolean,
    onDismissChangeDialog: () -> Unit = {},
    isRemoveMode: Boolean,
    onConfirmCategory: () -> Unit = {},
    onRemoveCategory: (String) -> Unit = {},
    onRemoveFlashcard: (String) -> Unit = {},
    onDismissCategoryDialog: () -> Unit = {},
    onToggleRemoveMode: () -> Unit = {},
    onSave: () -> Unit = {},
    onEditFlashcard: (String) -> Unit = {},
    onAddFlashcard: () -> Unit = {},
    onDismissAddFlashcardDialog: () -> Unit = {},
    onCreateFlashcardManually: () -> Unit = {},
    onConfirmDiscard: () -> Unit = {},
    onCreateFlashcardWithIA: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = onDismissCategoryDialog,
            title = {
                Text(
                    text = if (categoryBeingEdited == null) "Nova Categoria" else "Editar Categoria",
                    fontWeight = FontWeight.Bold,
                    color = DarkNavyBlue
                )
            },
            text = {
                CustomTextField(
                    inputValue = categoryText,
                    onInputValueChange = onCategoryTextChange,
                    placeholder = "Ex: Medicina, História..."
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirmCategory) {
                    Text("OK", fontWeight = FontWeight.Bold, color = DarkNavyBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissCategoryDialog) {
                    Text("CANCELAR", color = DarkGray)
                }
            },
            containerColor = White,
            shape = RoundedCornerShape(28.dp)
        )
    }

    if (showAddFlashcardDialog) {
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

    if (showChangeDialog) {
        BasicCustomAlertDialog(
            onDismissRequest = onDismissChangeDialog,
            onConfirmation = onConfirmDiscard,
            dialogTitle = "Alterações não salvas",
            dialogText = "Você possui alterações não salvas. Deseja realmente sair e descartá-las?",
            confirmationButtonText = "Sair e descartar",
            dismissButtonText = "Continuar editando",
        )
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        containerColor = Color.Transparent,
        topBar = {
            NavigationHeader(
                title = if (isEditMode) "Editar Baralho" else "Novo Baralho",
                onBackClick = onNavigateBack
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

                NeonActionButton(
                    text = "ADICIONAR FLASHCARD",
                    icon = R.drawable.ic_add,
                    onClickButton = onAddFlashcard,
                    modifier = Modifier.fillMaxWidth()
                )

                if (deckFlashcards.isNotEmpty()) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        Text(
                            text = "Conteúdo do Baralho",
                            color = DarkGray,
                            fontWeight = FontWeight.SemiBold,
                        )
                        TextButton(
                            onClick = { onToggleRemoveMode() },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = if (isRemoveMode) "VOLTAR PARA SELEÇÃO" else "GERENCIAR",
                                color = DarkNavyBlue,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
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
                                        DeleteButton(onClick = { onRemoveFlashcard(card.id) })
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
                } else
                    EmptyContent()
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeckEditorContentPreview() {
    CogniLinkTheme {
        val deck = PreviewDataProvider.deck
        val flashcards = PreviewDataProvider.flashcardList
            .filter { it.deckId == deck.id }
            .map { FlashcardWithStats(it, null) }

        DeckEditorContent(
            isEditMode = true,
            deckName = deck.name,
            deckDescription = deck.description,
            deckCategories = deck.categories,
            deckFlashcards = flashcards,
            showCategoryDialog = false,
            categoryBeingEdited = null,
            categoryText = "",
            isRemoveMode = false,
            showAddFlashcardDialog = false,
            showChangeDialog = false,
        )

    }
}
