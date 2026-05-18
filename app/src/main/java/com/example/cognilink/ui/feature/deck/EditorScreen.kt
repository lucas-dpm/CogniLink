package com.example.cognilink.ui.feature.deck

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
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
import com.example.cognilink.domain.Flashcard
import com.example.cognilink.domain.deck1
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.buttons.NeonActionButton
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.components.deck.DeckCardItem
import com.example.cognilink.ui.components.deck.EditDeckContent
import com.example.cognilink.ui.components.deck.EmptyDeckContent
import com.example.cognilink.viewmodel.DeckEditorUiState
import com.example.cognilink.viewmodel.DeckEditorViewModel


@Composable
fun DeckEditorScreen(
    viewModel: DeckEditorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeckEditorContent(
        isEditMode = true,
        uiState = uiState,
        onToggleRemoveMode = viewModel::toggleRemoveMode,
        onDeckNameChange = viewModel::onDeckNameChange,
        onDeckDescriptionChange = viewModel::onDeckDescriptionChange,
        onCategoryTextChange = viewModel::onCategoryTextChange,
        onAddCategory = { viewModel.openCategoryDialog() },
        onEditCategory = { viewModel.openCategoryDialog(it) },
        onRemoveCategory = viewModel::removeCategory,
        onConfirmCategory = viewModel::handleCategoryConfirmation,
        onDismissCategoryDialog = viewModel::closeCategoryDialog,
        onSaveClick = viewModel::saveDeck,
        onAddFlashcardClick = { /* TODO: Navegar para criação de card */ }
    )
}

@Composable
fun DeckEditorContent(
    isEditMode: Boolean,
    uiState: DeckEditorUiState,
    onDeckNameChange: (String) -> Unit,
    onDeckDescriptionChange: (String) -> Unit,
    onCategoryTextChange: (String) -> Unit,
    onAddCategory: () -> Unit,
    onEditCategory: (String) -> Unit,
    onRemoveCategory: (String) -> Unit,
    onConfirmCategory: () -> Unit,
    onDismissCategoryDialog: () -> Unit,
    onToggleRemoveMode: () -> Unit,
    onSaveClick: () -> Unit,
    onAddFlashcardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    if (uiState.showCategoryDialog) {
        AlertDialog(
            onDismissRequest = onDismissCategoryDialog,
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

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                SimpleGradientButton(
                    text = if(isEditMode) "SALVAR" else "CRIAR",
                    height = 40.dp,
                    icon = R.drawable.ic_check,
                    onClickButton = onSaveClick
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
                title = if (isEditMode) "Editar Baralho" else "Novo Baralho",
            )
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EditDeckContent(
                    name = uiState.deckName,
                    onNameChange = onDeckNameChange,
                    categories = uiState.deckCategories,
                    onCategoryClickRemove = onRemoveCategory,
                    onCategoryClickAdd = onAddCategory,
                    onCategoryClickEdit = onEditCategory,
                    description = uiState.deckDescription,
                    onDescriptionChange = onDeckDescriptionChange
                )

                NeonActionButton(
                    text = "ADICIONAR FLASHCARD",
                    icon = R.drawable.ic_add,
                    onClickButton = onAddFlashcardClick
                )

                if(uiState.deckFlashcards.isNotEmpty()) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        Text(
                            text = "Conteúdo do Baralho" ,
                            color = DarkGray,
                            fontWeight = FontWeight.SemiBold,
                        )
                        TextButton(
                            onClick = { onToggleRemoveMode() },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = if (uiState.isRemoveMode) "VOLTAR PARA SELEÇÃO" else "GERENCIAR",
                                color = DarkNavyBlue,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiState.deckFlashcards.forEach { card ->
                            DeckCardItem(flashcardType = card.cardType, flashcardQuestion = card.question, nextReview = "10", onSelectCard = {})
                        }
                    }
                }
                else
                    EmptyDeckContent()
            }
        }
    }
}



@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeckEditorContentPreview() {
    CogniLinkTheme {
        val uiState = DeckEditorUiState(
            deckName = deck1.name,
            deckDescription = deck1.description,
            deckCategories = deck1.categories,
            deckFlashcards = deck1.flashcards
        )

        DeckEditorContent(
            isEditMode = true,
            uiState = uiState,
            onDeckNameChange = {},
            onDeckDescriptionChange = {},
            onCategoryTextChange = {},
            onAddCategory = {},
            onEditCategory = {},
            onRemoveCategory = {},
            onConfirmCategory = {},
            onDismissCategoryDialog = {},
            onToggleRemoveMode = {},
            onSaveClick = {},
            onAddFlashcardClick = {}
        )

    }
}
