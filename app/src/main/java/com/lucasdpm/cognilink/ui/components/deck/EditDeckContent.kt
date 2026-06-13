package com.lucasdpm.cognilink.ui.components.deck

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.input.CustomTextField
import com.lucasdpm.cognilink.ui.components.utils.labels.CustomLabel
import com.lucasdpm.cognilink.ui.theme.DarkGray
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.MutedBlue

@Composable
fun EditDeckContent(
    name: String,
    onNameChange: (String) -> Unit,
    nameError: String? = null,
    categories: List<String>,
    onCategoryClickRemove: (String) -> Unit,
    onCategoryClickAdd: () -> Unit,
    onCategoryClickEdit: (String) -> Unit,
    description:String,
    onDescriptionChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)){
        CustomTextField(
            inputValue = name,
            onInputValueChange = onNameChange,
            label = {
                CustomLabel(
                    text = "Nome do baralho",
                    textColor = DarkGray,

                )
            },
            placeholder = "Nome do baralho",
            errorMessage = nameError
        )
        CustomTextField(
            inputValue = description,
            onInputValueChange = onDescriptionChange,
            label = {
                CustomLabel(
                    text = "Descrição",
                    textColor = DarkGray
                )
            },
            placeholder = "Descrição do baralho"
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            CustomLabel(
                text = "Categorias",
                textColor = DarkGray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = CenterVertically
            ) {
                categories.forEach { category ->
                    Surface(
                        color = MutedBlue,
                        shape = RoundedCornerShape(9999.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = category.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkNavyBlue,
                                modifier = Modifier.clickable { onCategoryClickEdit(category) }
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "Remover",
                                modifier = Modifier
                                    .size(10.dp)
                                    .clickable { onCategoryClickRemove(category) },
                                tint = DarkNavyBlue
                            )
                        }
                    }
                }

                if (categories.size < 3){
                    IconButton(
                        onClick = onCategoryClickAdd,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Adicionar Categoria",
                            tint = DarkNavyBlue
                        )
                    }}

            }
        }
    }
}