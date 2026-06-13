package com.lucasdpm.cognilink.ui.components.deck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.components.utils.ProgressBar
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.MutedBlue
import com.lucasdpm.cognilink.ui.theme.VeryLightGray
import com.lucasdpm.cognilink.ui.theme.White
import com.lucasdpm.cognilink.ui.theme.secondaryColor
import com.lucasdpm.cognilink.ui.theme.tertiaryColor

@Composable
fun ViewDeckContent(
    categories: List<String>?,
    difficulty: DifficultyLevel?,
    name: String?,
    description: String?,
    mastery: Float?,
    totalCards: Int?,
    cardToReview: Int?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp))
    {
        if (categories != null || difficulty != null)
        {
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if(!categories.isNullOrEmpty())
                    Surface(
                        color = MutedBlue,
                        shape = RoundedCornerShape(9999.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                            text = categories.joinToString(separator = " / ").uppercase(),
                            color = DarkNavyBlue.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                        )
                    }
                if(difficulty != null)
                    Surface(
                        color = difficulty.tertiaryColor,
                        shape = RoundedCornerShape(9999.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(difficulty.secondaryColor, CircleShape)
                            )
                            Text(
                                text = difficulty.toDisplayName(),
                                color = DarkGray.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                            )
                        }
                    }
            }
        }

        Text(
            text = name ?: "Carregando...",
            color = DarkNavyBlue,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 44.sp,
            lineHeight = 1.em
        )

        if(!description.isNullOrEmpty()){
            Text(
                text = description,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = DarkGray
            )
        }

        GradientSurface(
            shape = RoundedCornerShape(26.dp),
        )
        {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "DOMÍNIO ATUAL",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                            lineHeight = 12.sp
                )
                Text(
                    text = "${if (mastery != null ) (mastery * 100).toInt() else 0}%",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 12.sp
                )
                ProgressBar(
                    progressColor = White,
                    progress = mastery ?: 0f,
                    modifier = Modifier.height(8.dp)
                )
                if(mastery == null || mastery == 0f){
                    Text(
                        text = "Estude com flashcards nesse baralho para aumentar seu domínio",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        lineHeight = 12.sp
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp))
        {
            Surface(
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier.weight(0.5f),
                color = VeryLightGray.copy(alpha = 0.5f)
            )
            {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_deck),
                        contentDescription = null,
                        tint = DarkNavyBlue,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = "TOTAL DE CARDS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${totalCards ?: 0}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavyBlue
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier.weight(0.5f),
                color = VeryLightGray.copy(alpha = 0.5f)
            )
            {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_card_review),
                        contentDescription = null,
                        tint = DarkNavyBlue,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = "PARA REVISAR",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${cardToReview ?: 0}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavyBlue
                    )
                }
            }
        }
    }
}