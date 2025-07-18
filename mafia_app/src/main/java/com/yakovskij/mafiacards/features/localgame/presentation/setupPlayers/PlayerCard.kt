package com.yakovskij.mafiacards.features.localgame.presentation.setupPlayers


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yakovskij.mafiacards.core.ui.components.StyledCard


@Composable
fun PlayerCard(
    index: Int,
    name: String,
    onNameChange: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(name) }

    var showIconDialog by remember { mutableStateOf(false) }
    var selectedIcon by remember { mutableStateOf(Icons.Default.Person) }

    if (showIconDialog) {
        IconPickerDialog(
            currentIcon = selectedIcon,
            onConfirm = { icon ->
                selectedIcon = icon
                showIconDialog = false
            },
            onDismiss = {
                showIconDialog = false
            }
        )
    }


    StyledCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isEditing = true }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = selectedIcon,
                contentDescription = "Игрок",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(8.dp)
                    .clickable { showIconDialog = true }
            )

            Spacer(modifier = Modifier.width(12.dp))
            if (isEditing) {
                TextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default,
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = {
                    onNameChange(editedName.trim())
                    isEditing = false
                }) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Сохранить")
                }
            } else {
                Text(
                    text = if (name.isNotBlank()) name else "Игрок ${index + 1}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (name.isNotBlank()) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
                IconButton(onClick = {
                    editedName = name
                    isEditing = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редактировать имя"
                    )
                }
            }
        }
    }
}