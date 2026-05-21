package com.example.cognilink.ui.feature

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cognilink.R
import com.example.cognilink.domain.UserStats
import com.example.cognilink.domain.fakeUser
import com.example.cognilink.ui.components.utils.GradientSurface
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.ProgressBar
import com.example.cognilink.ui.components.utils.buttons.NeonActionButton
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.DarkRed
import com.example.cognilink.ui.theme.Green
import com.example.cognilink.ui.theme.LavenderBlue
import com.example.cognilink.ui.theme.MutedBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.Red
import com.example.cognilink.ui.theme.VeryLightRed
import com.example.cognilink.ui.theme.White
import com.example.cognilink.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.userStats?.let { stats ->
        ProfileContent(
            userName = uiState.userName,
            userRank = uiState.userRank,
            userStats = stats,
            cognitiveEfficiencyText = uiState.cognitiveEfficiencyText
        )
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    userName: String = "João Silva",
    userRank: String = "Iniciante",
    userStats: UserStats,
    cognitiveEfficiencyText: String = "Seu cérebro está absorvendo mais conteúdo em menos tempo.",
) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = modifier
            .padding()
            .statusBarsPadding(),
        topBar = {
            NavigationHeader(title = "Perfil")
        },
        containerColor = OffWhite
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    Text(
                        text = userName,
                        color = DarkNavyBlue,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(color = MutedBlue, shape = RoundedCornerShape(32.dp)) {
                        Row(
                            verticalAlignment = CenterVertically,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                tint = DarkNavyBlue,
                                contentDescription = "",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = userRank,
                                color = DarkGray,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Surface(
                    color = White,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        GradientSurface(shape = CircleShape) {
                            Surface(
                                color = White,
                                shape = CircleShape,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(110.dp) // Ou .aspectRatio(1f) para ser dinâmico
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = CenterHorizontally) {
                                        Text(
                                            text = "${userStats.cognitiveEfficiencyIndex}",
                                            fontSize = 48.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkNavyBlue,
                                        )
                                        Text(
                                            text = "ÍNDICE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.W700,
                                            color = DarkGray
                                        )
                                    }

                                }
                            }
                        }
                        Column(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = CenterHorizontally
                        ) {
                            Text(
                                text = "Eficiência Cognitiva",
                                color = DarkNavyBlue,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                modifier = Modifier.width(250.dp),
                                text = cognitiveEfficiencyText,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                GradientSurface(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = DarkNavyBlue.copy(alpha = 0.4f)
                        ) {
                            Icon(
                                modifier = Modifier.padding(14.dp),
                                painter = painterResource(id = R.drawable.ic_ray),
                                contentDescription = null,
                                tint = LavenderBlue
                            )
                        }
                        Text(
                            text = "VELOCIDADE MÉDIA DE RESPOSTA",
                            color = LavenderBlue,
                            fontWeight = FontWeight.W700,
                            fontSize = 14.sp
                        )
                        Text(
                            // TODO: Função que apresente o tempo formatado:
                            text = "${userStats.globalAverageLatencyMs}s",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = LavenderBlue,
                        )
                    }

                }
                Text(
                    text = "Saúde da Memória", color = DarkNavyBlue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = White,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "DOMÍNIO GERAL", color = DarkGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val strokeWidth = 8.dp.toPx()
                                    val arcLength = 35f
                                    val color = DarkNavyBlue
                                    listOf(0f, 90f, 180f, 270f).forEach { angle ->
                                        drawArc(
                                            color = color,
                                            startAngle = angle - arcLength / 2,
                                            sweepAngle = arcLength,
                                            useCenter = false,
                                            style = Stroke(
                                                width = strokeWidth,
                                                cap = StrokeCap.Round
                                            )
                                        )
                                    }
                                }
                                Text(
                                    text = "${(userStats.overallMastery * 100).toInt()}%",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkNavyBlue,
                                )
                            }
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = White,
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TAXA DE RETENÇÃO", color = DarkGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Column()
                            {
                                Text(
                                    text = "${(userStats.retentionRate * 100).toInt()}%",
                                    fontSize = 60.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkNavyBlue,
                                )
                                //TODO: Texto persolizado
                                Text(
                                    text = "Acima da média", color = DarkGray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        }
                    }
                }

                Surface(
                    color = White,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Volumetria", color = DarkNavyBlue,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                color = MutedBlue,
                                shape = CircleShape
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_trophy),
                                    contentDescription = null,
                                    modifier = Modifier.padding(8.dp),
                                    tint = DarkNavyBlue
                                )
                            }
                            Column {
                                Text(
                                    text = "${userStats.totalFlashcardsDone}", color = DarkGray,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Flashcards feitos", color = DarkGray.copy(alpha = 0.7f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Surface(
                                color = MutedBlue,
                                shape = CircleShape
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_fire),
                                    contentDescription = null,
                                    modifier = Modifier.padding(8.dp),
                                    tint = DarkNavyBlue
                                )
                            }
                            Column {
                                Text(
                                    text = "${userStats.totalFlashcardsReviewed}", color = DarkGray,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Flashcards revisados",
                                    color = DarkGray.copy(alpha = 0.7f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }


                Surface(
                    color = VeryLightRed.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(2.dp, VeryLightRed)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = CenterVertically
                        ) {
                            Surface(
                                color = VeryLightRed,
                                shape = CircleShape
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_warning),
                                    contentDescription = null,
                                    modifier = Modifier.padding(16.dp),
                                    tint = DarkRed
                                )
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "Sanguessugas Detectadas",
                                    color = DarkRed,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Há ${userStats.activeLeechesCount} conceitos travados no limbo. Eles precisam ser reestruturados para evitar a queda na taxa de rentenção",
                                    color = DarkGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                        Column(modifier = Modifier.width(200.dp)) {
                            NeonActionButton(
                                text = "Rever Sanguessugas",
                                height = 60.dp,
                                onClickButton = {}
                            )
                        }
                    }
                }
                Surface(color = White, shape = RoundedCornerShape(24.dp)) {
                    Column(
                        Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                color = MutedBlue,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_performance),
                                    contentDescription = null,
                                    modifier = Modifier.padding(10.dp),
                                    tint = DarkNavyBlue
                                )
                            }
                            Text(
                                text = "DESEMPENHO",
                                color = DarkGray.copy(alpha = 0.7f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = CenterVertically
                        ) {
                            Row(
                                verticalAlignment = CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Green, CircleShape)
                                )
                                Text(
                                    text = "ACERTOS",
                                    color = DarkGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W700
                                )
                            }
                            Text(
                                text = "${userStats.totalFlashcardsHits}",
                                color = DarkGray,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = CenterVertically
                        ) {
                            Row(
                                verticalAlignment = CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Red, CircleShape)
                                )
                                Text(
                                    text = "ERROS",
                                    color = DarkGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W700
                                )
                            }
                            Text(
                                text = "${userStats.totalFlashcardsMisses}",
                                color = DarkGray,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        ProgressBar(
                            progressColor = Green,
                            progress = (userStats.totalFlashcardsHits.toFloat() / (userStats.totalFlashcardsHits + userStats.totalFlashcardsMisses)),
                            backgroundColor = Red
                        )
                    }
                }
                Surface(color = White, shape = RoundedCornerShape(24.dp)) {
                    Column(
                        Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                color = MutedBlue,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_clock),
                                    contentDescription = null,
                                    modifier = Modifier.padding(10.dp),
                                    tint = DarkNavyBlue
                                )
                            }
                            Text(
                                text = "TEMPO DE ESTUDO",
                                color = DarkGray.copy(alpha = 0.7f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                        }
                        //TODO: formatar tempo
                        Text(
                            text = "${userStats.totalStudyTime} horas",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkGray
                        )
                        Surface(color = MutedBlue, shape = RoundedCornerShape(28.dp)) {
                            Row(Modifier.padding(10.dp), verticalAlignment = CenterVertically) {
                                Icon(
                                    modifier = Modifier.padding(end = 6.dp),
                                    painter = painterResource(id = R.drawable.ic_reclock),
                                    contentDescription = null,
                                    tint = DarkNavyBlue
                                )
                                Text(
                                    //TODO: formatar data e texto
                                    text = "${userStats.lastReview} atrás Última Revisão",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W700,
                                    color = DarkNavyBlue
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileContentPreview() {
    ProfileContent(userStats = fakeUser.stats)
}

