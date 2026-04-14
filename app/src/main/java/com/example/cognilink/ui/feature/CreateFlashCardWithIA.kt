package com.example.cognilink.ui.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.ui.components.TextInput
import com.example.cognilink.ui.components.TopBar
import com.example.cognilink.ui.components.card.DifficultySelector
import com.example.cognilink.ui.components.card.FileUploadArea
import com.example.cognilink.ui.components.card.FlashcardTypeSelector
import com.example.cognilink.ui.components.card.QuantitySelector
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Gray
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VividCyan
import com.example.cognilink.ui.theme.White

fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp = 2.dp,
    dashWidth: Dp = 8.dp,
    gapWidth: Dp = 4.dp,
    cornerRadius: Dp = 0.dp
) = this.drawBehind {
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashWidth.toPx(), gapWidth.toPx()),
            phase = 0f
        )
    )

    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = CornerRadius(cornerRadius.toPx())
    )
}

@Composable
fun CreateFlashcardWithIAScreen(
    modifier: Modifier = Modifier,
    flashcardTheme: String = "",
    onFlashcardThemeChange: (String) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var quantity by remember { mutableIntStateOf(1) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .statusBarsPadding(),
        topBar = { TopBar(title = "CRIAR NOVO FLASHCARD") },
        containerColor = OffWhite
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // --- SEÇÃO TEMA ---
            Column {
                TextInput(
                    inputValue = flashcardTheme,
                    onInputValueChange = onFlashcardThemeChange,
                    label = "Tema do Flashcard",
                    placeholder = "Ex: Mitocôndrias e Ciclo de Krebs",
                    keyboardType = KeyboardType.Text
                )
                ObservationText(text = "Se nulo, a IA criará com base no arquivo anexo. O tema será preenchido após a criação.")
            }

            // --- SEÇÃO ANEXO ---
            Column {
                Text(text = "Anexo de Arquivo (Opcional)", color = DarkGray, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))

                FileUploadArea(onUploadClick = { /* TODO */ })

                ObservationText(text = "Se nulo, a IA criará com base no tema. Pelo menos o TEMA ou um ARQUIVO deve ser fornecido.")
            }

            // --- SEÇÃO SELETOR ---
            FlashcardTypeSelector(selectedOption = null, onOptionSelected = { })

            // --- SEÇÃO CONFIGURAÇÕES (Dificuldade e Quantidade) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel("Dificuldade")
                    DifficultySelector(
                        difficultyLevels = listOf(DifficultyLevel.EAZY, DifficultyLevel.MEDIUM, DifficultyLevel.HARD),
                        selectedDifficulty = DifficultyLevel.EAZY,
                        onDifficultySelected = { }
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel("Quantidade")
                    QuantitySelector(
                        quantity = quantity,
                        onQuantityChange = { quantity = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 12.dp)) {
                    Icon(painter = painterResource(id = R.drawable.ic_stars),
                        contentDescription = null,
                        tint = VividCyan,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text("GERAR FLASHCARDS COM IA",
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }

            }
        }
    }
}


@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = DarkGray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun ObservationText(text: String) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = DarkNavyBlue, fontWeight = FontWeight.Bold)) {
            append("OBS: ")
        }
        append(text)
    }
    Text(
        text = annotatedString,
        color = Gray,
        fontSize = 11.sp,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Preview
@Composable
private fun CreateFlashcardWithIAScreenPreview() {
    CogniLinkTheme {
        CreateFlashcardWithIAScreen()
    }
}