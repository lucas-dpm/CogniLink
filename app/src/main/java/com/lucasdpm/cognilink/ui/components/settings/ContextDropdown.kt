package com.lucasdpm.cognilink.ui.components.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.data.model.StudyContext
import com.lucasdpm.cognilink.data.preview.PreviewDataProvider
import com.lucasdpm.cognilink.ui.components.utils.GradientSurface
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.LightGray
import com.lucasdpm.cognilink.ui.theme.Red
import com.lucasdpm.cognilink.ui.theme.White

@Composable
fun ContextDropdown(
    contexts: List<StudyContext>,
    onEdit: (String) -> Unit,
    onDelete: (StudyContext) -> Unit,
    onAdd: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    GradientSurface(shape = RoundedCornerShape(12.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_loc),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Meus Contextos",
                    color = White,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_keyboard_arrow_down),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(if (expanded) 180f else 0f)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Surface(
                    contentColor = White,
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                ) {
                    Column {
                        if (contexts.isEmpty()) {
                            Text(
                                text = "Nenhum contexto cadastrado",
                                color = DarkGray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )
                        } else {
                            contexts.forEach { context ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = context.name,
                                        color = DarkGray,
                                        fontSize = 16.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Row {
                                        IconButton(onClick = { onEdit(context.id) }) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_edit),
                                                contentDescription = "Editar",
                                                tint = DarkGray,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        IconButton(onClick = { onDelete(context) }) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_delete),
                                                contentDescription = "Excluir",
                                                tint = Red,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp),
                                    thickness = 0.5.dp,
                                    color = LightGray
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp, top = 6.dp).height(40.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SimpleGradientButton(
                                    text = "Adicionar localização".uppercase(),
                                    icon = R.drawable.ic_add,
                                    onClickButton = onAdd,
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
fun ContextDropdownPreview() {
    CogniLinkTheme {
        ContextDropdown(
            contexts = PreviewDataProvider.studyContextList,
            onEdit = {},
            onDelete = {},
            onAdd = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContextDropdownEmptyPreview() {
    CogniLinkTheme {
        ContextDropdown(
            contexts = emptyList(),
            onEdit = {},
            onDelete = {},
            onAdd = {}
        )
    }
}
