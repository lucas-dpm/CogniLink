package com.lucasdpm.cognilink.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.MutedBlue
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    userName: String = "João Silva",
    homeInsight: String = "Você está indo muito bem!",
    overallMastery: Float = 0.8f,
    totalStudyTime: String = "20 dias",
    cardsDone: Int = 1200,
    learnRetention: Float = 0.8f,
    onOpenProfileClick: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        GradientSurface(
            shape = RoundedCornerShape(24.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "BEM VINDO DE VOLTA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = White.copy(alpha = 0.7f)
                )
                Text(
                    text = userName,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = White
                )
                Text(
                    text = homeInsight,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = White.copy(alpha = 0.7f)
                )

            }
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Dashboard",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "VER PERFIL",
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onOpenProfileClick() }
            )
        }



        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Surface(
                    color = White,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(0.5f).fillMaxHeight(),
                    shadowElevation = 4.dp
                ) {
                    Column (modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = MutedBlue,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(painter = painterResource(id = R.drawable.ic_learn),
                                    contentDescription = null,
                                    tint = DarkNavyBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        val masteryPercentage = (overallMastery * 100).toInt()
                        Text(text = "$masteryPercentage%",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 36.sp,)
                        Text(text = "DOMÍNIO GERAL",
                            fontWeight = FontWeight.Bold ,
                            fontSize = 12.sp,
                            color = DarkGray.copy(alpha = 0.5f),
                            lineHeight = 12.sp)
                    }
                }
                Surface(
                    color = White,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(0.5f).fillMaxHeight(),
                    shadowElevation = 4.dp
                ) {
                    Column (modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = MutedBlue,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(painter = painterResource(id = R.drawable.ic_clock),
                                    contentDescription = null,
                                    tint = DarkNavyBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(text = totalStudyTime,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 36.sp,
                        )
                        Text(text = "TEMPO DE ESTUDO",
                            fontWeight = FontWeight.Bold ,
                            fontSize = 12.sp,
                            color = DarkGray.copy(alpha = 0.5f),
                            lineHeight = 12.sp)
                    }
                }
            }
            Row(
                modifier = Modifier.height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Surface(
                    color = White,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(0.5f).fillMaxHeight(),
                    shadowElevation = 4.dp
                ) {
                    Column (modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = MutedBlue,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(painter = painterResource(id = R.drawable.ic_deck),
                                    contentDescription = null,
                                    tint = DarkNavyBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(text = cardsDone.toString(),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 36.sp,)
                        Text(text = "CARDS CONCLUÍDOS",
                            fontWeight = FontWeight.Bold ,
                            fontSize = 12.sp,
                            color = DarkGray.copy(alpha = 0.5f),
                            lineHeight = 12.sp)
                    }
                }
                Surface(
                    color = White,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(0.5f).fillMaxHeight(),
                    shadowElevation = 4.dp
                ) {
                    Column (modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = MutedBlue,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(painter = painterResource(id = R.drawable.ic_rate),
                                    contentDescription = null,
                                    tint = DarkNavyBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        val learnRetentionPercentage = (learnRetention * 100).toInt()
                        Text(text = "$learnRetentionPercentage%",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 36.sp,
                            )
                        Text(text = "RETENÇÃO MÉDIA",
                            fontWeight = FontWeight.Bold ,
                            fontSize = 12.sp,
                            color = DarkGray.copy(alpha = 0.5f),
                            lineHeight = 12.sp)
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
        ProfileSection(onOpenProfileClick = {})
    }
}