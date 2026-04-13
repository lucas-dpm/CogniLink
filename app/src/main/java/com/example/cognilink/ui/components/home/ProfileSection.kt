package com.example.cognilink.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Green
import com.example.cognilink.ui.theme.MutedBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VeryLightGray
import com.example.cognilink.ui.theme.White


@Composable
fun ProfileSection(
    userName: String = "João Silva",
    overallMastery: Float = 0.8f,
    dayStreak: Int = 10,
    cardsDone: Int = 1200,
    learnRetention: Float = 0.8f,
    modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = White,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = "PERFIL DE APRENDIZAGEM", color = DarkGray)
                    Text(text = userName, color = DarkNavyBlue, fontSize = 30.sp,fontWeight = FontWeight.ExtraBold)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "DOMÍNO GERAL",
                        color = DarkGray,
                        fontWeight = FontWeight.SemiBold
                    )
                    val overallPercentage = overallMastery * 100
                    Text(
                        text = "$overallPercentage%",
                        color = DarkNavyBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()
                ) {
                    if(overallMastery > 0f){
                        Surface(modifier = Modifier.weight(overallMastery).height(6.dp),
                            color = DarkNavyBlue,shape = RoundedCornerShape(9999.dp)
                        ){}
                        Surface(modifier = Modifier.weight(1 - overallMastery).height(6.dp),
                            color = VeryLightGray,shape = RoundedCornerShape(topEnd = 9999.dp, bottomEnd = 9999.dp)
                        ){}
                    }
                    else{
                        Surface(modifier = Modifier.weight(1f).height(6.dp),
                            color = VeryLightGray,shape = RoundedCornerShape(topEnd = 9999.dp, bottomEnd = 9999.dp)
                        ){}
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Surface(
                    color = OffWhite,
                    shape = RoundedCornerShape(24.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "SINCRONIZAÇÃO ATUAL", color = DarkGray, fontWeight = FontWeight.Medium, fontSize = 10.sp)
                            Text(text = "SEQUÊNCIA DE $dayStreak DIAS", color = DarkGray, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Surface(color = MutedBlue, shape = RoundedCornerShape(4.dp)){
                            Icon(painter = painterResource(id= R.drawable.ic_calendar),
                                contentDescription = null,
                                tint = DarkNavyBlue,
                                modifier = Modifier.padding(10.dp).size(30.dp)
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Surface(
                        color = OffWhite,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Column (modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(painter = painterResource(id = R.drawable.ic_cards_done),
                                contentDescription = null,
                                tint = Green
                            )
                            Text(text = cardsDone.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = DarkNavyBlue)
                            Text(text = "CARDS",
                                fontWeight = FontWeight.Medium ,
                                fontSize = 11.sp,
                                color = DarkGray)
                        }
                    }
                    Surface(
                        color = MutedBlue,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Column (modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(painter = painterResource(id = R.drawable.ic_cognition),
                                contentDescription = null,
                                tint = DarkNavyBlue
                            )
                            val learnRetentionPercentage = learnRetention * 100
                            Text(text = "$learnRetentionPercentage%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = DarkNavyBlue)
                            Text(text = "TAXA DE RENTENÇÃO",
                                fontWeight = FontWeight.Medium ,
                                fontSize = 11.sp,
                                color = DarkGray)
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