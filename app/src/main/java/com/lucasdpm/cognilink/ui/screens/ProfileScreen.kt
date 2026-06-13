package com.lucasdpm.cognilink.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.CircularProgressIndicator
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
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.data.model.UserStats
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.components.utils.NavigationHeader
import com.lucasdpm.cognilink.ui.components.utils.ProgressBar
import com.lucasdpm.cognilink.ui.components.utils.buttons.NeonActionButton
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.DarkRed
import com.lucasdpm.cognilink.ui.theme.Green
import com.lucasdpm.cognilink.ui.theme.LavenderBlue
import com.lucasdpm.cognilink.ui.theme.MutedBlue
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.theme.Red
import com.lucasdpm.cognilink.ui.theme.VeryLightRed
import com.lucasdpm.cognilink.ui.theme.White
import com.lucasdpm.cognilink.ui.states.ProfileUiState
import com.lucasdpm.cognilink.ui.viewmodels.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

import androidx.compose.runtime.LaunchedEffect

@Composable
fun ProfileScreen(
    userId: String,
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.initialize(userId)
    }

    when (val state = uiState) {
        is ProfileUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ProfileUiState.Success -> {
            ProfileContent(
                userName = state.userName,
                userRank = state.ranking.currentRank.displayName,
                userStats = state.stats,
                cognitiveEfficiencyInsight = state.ranking.efficiencyInsight,
                globalAverageLatencyMsText = viewModel.formatLatency(state.stats.globalAverageLatencyMs),
                retentionRateInsight = state.ranking.retentionInsight,
                formattedStudyTime = viewModel.formatTime(state.stats.totalStudyTime),
                formattedLastReview = viewModel.formatLastReview(state.stats.lastReview),
                onNavigateBack = onNavigateBack,
                onReviewLeeches = { /* TODO: Navigate to FlashcardPlayer with leech filter */ }
            )
        }
        is ProfileUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message, color = Red)
            }
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    userName: String = "Alex Silva",
    userRank: String = "Iniciante",
    userStats: UserStats,
    cognitiveEfficiencyInsight: String,
    globalAverageLatencyMsText: String,
    retentionRateInsight: String,
    formattedStudyTime: String,
    formattedLastReview: String,
    onNavigateBack: () -> Unit,
    onReviewLeeches: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = modifier
            .padding()
            .statusBarsPadding(),
        topBar = {
            NavigationHeader(title = "Perfil", onBackClick = onNavigateBack)
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
                                    .size(110.dp)
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
                                text = cognitiveEfficiencyInsight,
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
                            text = "TEMPO MÉDIA DE RESPOSTA",
                            color = LavenderBlue,
                            fontWeight = FontWeight.W700,
                            fontSize = 14.sp
                        )
                        Text(
                            text = globalAverageLatencyMsText,
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
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = White,
                        modifier = Modifier.weight(0.5f).height(200.dp)
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
                            .height(200.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TAXA DE RETENÇÃO", color = DarkGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 12.sp
                            )
                            Column()
                            {
                                Text(
                                    text = "${(userStats.retentionRate * 100).toInt()}%",
                                    fontSize = 60.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkNavyBlue,
                                )
                                Text(
                                    text = retentionRateInsight, color = DarkGray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 10.sp
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
                                    text = "Há ${userStats.activeLeechesCount} conceitos travados no limbo. Eles precisam ser reestruturados para evitar a queda na taxa de retenção",
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
                                onClickButton = onReviewLeeches,
                                modifier = Modifier.fillMaxWidth()
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
                            Text(
                                text = formattedStudyTime,
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
                                    text = formattedLastReview,
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
    ProfileContent(
        userStats = UserStats(
            userId = "123",
            cognitiveEfficiencyIndex = 85f,
            globalAverageLatencyMs = 450L,
            overallMastery = 0.75f,
            retentionRate = 0.92f,
            totalFlashcardsDone = 1250,
            totalFlashcardsReviewed = 850,
            activeLeechesCount = 3,
            totalFlashcardsHits = 1100,
            totalFlashcardsMisses = 150
        ),
        globalAverageLatencyMsText = "450ms",
        cognitiveEfficiencyInsight = "Seu cérebro está absorvendo mais conteúdo em menos tempo.",
        retentionRateInsight = "Considere revisar os cartões com maior frequência.",
        formattedStudyTime = "12h 30min",
        formattedLastReview = "2h atrás Última Revisão",
        onNavigateBack = {}
    )
}
