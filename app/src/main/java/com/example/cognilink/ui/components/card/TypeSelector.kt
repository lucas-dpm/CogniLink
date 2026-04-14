package com.example.cognilink.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray

/*TODO*/
data class FlashcardOption(
    val id: Int,
    val title: String,
    val description: String,
    val icon: Int
)

@Composable
fun FlashcardTypeSelector(
    modifier: Modifier = Modifier,
    selectedOption: FlashcardOption? = null,
    onOptionSelected: (FlashcardOption) -> Unit
) {
    // 2. Lista de opções
    val options = remember {
        listOf(
            FlashcardOption(1, "Pergunta e Resposta", "Ideal para fatos diretos", R.drawable.ic_basic_card),
            FlashcardOption(2, "Múltipla Escolha", "Ótimo para exames", R.drawable.ic_multiple_choice_card),
            FlashcardOption(3,"Omissão de Palavras", "Cloze deletion para memorização de contexto", R.drawable.ic_cloze_card),
            FlashcardOption(4, "Verdadeiro ou Falso", "Decisões rápidas e validação de conceitos", R.drawable.ic_true_or_false_card),
            FlashcardOption(5, "Chat de Feynman", "Explique conceitos complexos de forma simples", R.drawable.ic_chat_feynman),
            FlashcardOption(6,"Aleatório","Misture todos os estilos para máximo desafio", R.drawable.ic_die)
        )
    }

    var selectedId by remember { mutableIntStateOf(1) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Tipo de Flashcard",
            fontWeight = FontWeight.Bold,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        options.forEach { option ->
            TypeItem(
                typeName = option.title,
                typeDefinition = option.description,
                icon = option.icon,
                checked = option.id == selectedId,
                onSelect = { selectedId = option.id }
            )
        }
    }
}

@Preview
@Composable
private fun FlashcardTypeSelectorPreview() {
    CogniLinkTheme {
        FlashcardTypeSelector(selectedOption = null, onOptionSelected = {})
    }
}