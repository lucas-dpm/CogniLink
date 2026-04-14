package com.example.cognilink.ui.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.ui.components.NeonFAB
import com.example.cognilink.ui.components.TopBar
import com.example.cognilink.ui.components.TextInput
import com.example.cognilink.ui.components.neonGlow
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LavenderBlue
import com.example.cognilink.ui.theme.VividCyan
import com.example.cognilink.ui.theme.White

@Composable
fun CreateDeckScreen(
    modifier: Modifier = Modifier,
    deckName: String = "",
    onDeckNameChange: (String) -> Unit = {},
    categoryName: String = "",
    onCategoryNameChange: (String) -> Unit = {}
) {

    val scrollState = rememberScrollState()

    Scaffold(
        floatingActionButton = {
            NeonFAB(neonColor = VividCyan,
                backgroundColor = DarkNavyBlue,
                buttonDescription = "Criar baralho de flashcards",
                iconColor = VividCyan,
                icon = R.drawable.ic_check,
                onClick = { /* TODO */ }
            )
        }
    ) {padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(padding),
        ) {
            TopBar(title = "CRIAR NOVO BARALHO")

            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Text(text = "INFORMAÇÕES BÁSICAS", fontWeight = FontWeight.Bold)
                TextInput(
                    modifier = Modifier,
                    inputValue = deckName,
                    onInputValueChange = onDeckNameChange,
                    label = "Nome do baralho",
                    placeholder = "Ex: Matemática Básica",
                    keyboardType = KeyboardType.Text
                )
                TextInput(
                    modifier = Modifier,
                    inputValue = categoryName,
                    onInputValueChange = onCategoryNameChange,
                    label = "Categoria",
                    placeholder = "Ex: Matemática",
                    keyboardType = KeyboardType.Text
                )
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.height(80.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {

                        Row(modifier = Modifier
                            .fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(shape = CircleShape,
                                color = VividCyan,
                                modifier = Modifier
                                    .neonGlow(color = VividCyan,
                                        borderRadius = 24.dp,
                                        glowRadius = 5.dp
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add),
                                    contentDescription = "Adicionar flashcard",
                                    tint = DarkNavyBlue,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Text(text = "ADICIONAR FLASHCARD",
                                color = White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 20.dp))
                        }

                }

                Surface(
                    color = White,
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_deck),
                            contentDescription = "Flashcard",
                            tint = LavenderBlue,
                            modifier = Modifier.size(60.dp),
                            )
                        Text(text = "Seu baralho está vazio!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(text = "Comece a construir seu conhecimento adicionando seu primeiro cartão de estudo acima.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            color = DarkGray,
                        )
                        Row(modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_overlay),
                                    contentDescription = null,
                                    tint = LavenderBlue.copy(alpha = 0.8f),
                                    modifier = Modifier.size(8.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_overlay),
                                    contentDescription = null,
                                    tint = LavenderBlue.copy(alpha = 0.4f),
                                    modifier = Modifier.size(8.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_overlay),
                                    contentDescription = null,
                                    tint = LavenderBlue.copy(alpha = 0.2f),
                                    modifier = Modifier.size(8.dp)
                                )
                            }
                        }
                    }
                }


            }

        }
    }

}

@Preview
@Composable
private fun CreateDeckScreenPreview() {
    CogniLinkTheme {
        CreateDeckScreen()
    }
}