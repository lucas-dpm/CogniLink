package com.example.cognilink.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.ui.components.utils.GradientSurface
import com.example.cognilink.ui.components.utils.ProgressBar
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.MutedBlue
import com.example.cognilink.ui.theme.White


@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    userName: String = "João Silva",
    overallMastery: Float = 0.8f,
    totalStudyTime: String = "20 dias",
    cardsDone: Int = 1200,
    learnRetention: Float = 0.8f,
    ) {

    GradientSurface(
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
    )
    {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = modifier.padding(all = 20.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "PERFIL DE APRENDIZAGEM", 
                        color = White.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userName, 
                        color = White, 
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "DOMÍNIO GERAL",
                        color = White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${(overallMastery * 100).toInt()}%",
                        color = White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }

                ProgressBar(
                    progress = overallMastery,
                    progressColor = White,
                    backgroundColor = White.copy(alpha = 0.2f),
                    modifier = Modifier.height(8.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Surface(
                    color = White,
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "TEMPO TOTAL DE ESTUDO",
                                color = DarkGray.copy(alpha = 0.6f), 
                                fontWeight = FontWeight.Bold, 
                                fontSize = 10.sp
                            )
                            Text(
                                text = totalStudyTime,
                                color = DarkNavyBlue,
                                fontWeight = FontWeight.ExtraBold, 
                                fontSize = 18.sp
                            )
                        }
                        Surface(
                            color = MutedBlue,
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Icon(
                                painter = painterResource(id= R.drawable.ic_calendar),
                                contentDescription = null,
                                tint = DarkNavyBlue,
                                modifier = Modifier.padding(8.dp).size(24.dp)
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Surface(
                        color = White,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(0.5f),
                        shadowElevation = 4.dp
                    ) {
                        Column (modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Surface(
                                color = MutedBlue,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(painter = painterResource(id = R.drawable.ic_cards_done),
                                        contentDescription = null,
                                        tint = DarkNavyBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Text(text = cardsDone.toString(),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = DarkNavyBlue)
                            Text(text = "CARDS CONCLUÍDOS",
                                fontWeight = FontWeight.Bold ,
                                fontSize = 9.sp,
                                color = DarkGray.copy(alpha = 0.5f))
                        }
                    }
                    Surface(
                        color = White,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(0.5f),
                        shadowElevation = 4.dp
                    ) {
                        Column (modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Surface(
                                color = MutedBlue,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(painter = painterResource(id = R.drawable.ic_cognition),
                                        contentDescription = null,
                                        tint = DarkNavyBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            val learnRetentionPercentage = (learnRetention * 100).toInt()
                            Text(text = "$learnRetentionPercentage%",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = DarkNavyBlue)
                            Text(text = "RETENÇÃO MÉDIA",
                                fontWeight = FontWeight.Bold ,
                                fontSize = 9.sp,
                                color = DarkGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileSectionPreview() {
    CogniLinkTheme {
        ProfileSection()
    }
}