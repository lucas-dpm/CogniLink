package com.lucasdpm.cognilink.ui.components.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.ui.components.utils.labels.CustomLabel
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme

@Composable
fun TypeOptionList(
    options: List<FlashcardType>,
    selectedOption: FlashcardType?,
    onOptionSelected: (FlashcardType?) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomLabel(text = "Tipo de Flashcard")

        options.forEach { type ->
            TypeItem(
                typeName = type.getDisplayName(),
                typeDefinition = type.getDefinition(),
                icon = type.getIcon(),
                checked = type == selectedOption,
                onSelect = { onOptionSelected(type) }
            )
        }
        TypeItem(
            typeName = "Aleatório",
            typeDefinition = "Misture todos os estilos para máximo desafio",
            icon = R.drawable.ic_die,
            checked = selectedOption == null,
            onSelect = { onOptionSelected(null) }
        )
    }
}

@Preview
@Composable
private fun TypeOptionListPreview() {
    CogniLinkTheme {
        val options = listOf(FlashcardType.BASIC, FlashcardType.MULTIPLE_CHOICE, FlashcardType.OMISSION, FlashcardType.TRUE_OR_FALSE, FlashcardType.CHAT_FEYNMAN )
        var selectedOption by remember { mutableStateOf<FlashcardType?>(null) }

        TypeOptionList(
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = { option -> selectedOption = option }
        )
    }
}