package com.yakovskij.mafiacards.features.localgame.presentation.setupDeck


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafia_engine.domain.RoleType

@Composable
fun DeckScreen(
    viewModel: DeckViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState

    val snackbarHostState = remember { SnackbarHostState() }
    // Показываем Snackbar, если ошибка обновилась
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                DeckScreenContent(
                    totalPlayers = uiState.totalPlayers,
                    mafiaCount = uiState.mafiaCount,
                    doctorCount = uiState.doctorCount,
                    detectiveCount = uiState.detectiveCount,
                    errorMessage = uiState.errorMessage,
                    isValueCorrect = uiState.isValueCorrect,
                    onRoleChange = viewModel::onRoleCountChange,
                    onBack = onBack
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .align(Alignment.TopCenter)
        ) {
            SnackbarHost(hostState = snackbarHostState)
        }
    }

}
@Composable
fun DeckScreenContent(
    totalPlayers: Int,
    mafiaCount: Int,
    doctorCount: Int,
    detectiveCount: Int,
    errorMessage: String?,
    isValueCorrect: Boolean,
    onRoleChange: (RoleType, Int) -> Unit,
    onBack: () -> Unit
) {
    val roles = listOf(
        RoleType.MAFIA to mafiaCount,
        RoleType.DOCTOR to doctorCount,
        RoleType.DETECTIVE to detectiveCount,
    )
    val specialCount = mafiaCount + doctorCount + detectiveCount
    val civilians = (totalPlayers - specialCount).coerceAtLeast(0)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Колода", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        RoleCard("Мирный", civilians, isLocked = true)

        roles.forEach { (role, count) ->
            RoleCard(role.title, count, isLocked = false) {
                onRoleChange(role, it)
            }
        }

        if (!isValueCorrect) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Карточки заполнены неправильно",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.weight(1f))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth(), enabled = isValueCorrect) {
            Text("Назад")
        }
    }
}



@Composable
fun RoleCard(
    name: String,
    count: Int,
    isLocked: Boolean,
    onCountChange: ((Int) -> Unit)? = null
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (count == 0) Color.LightGray.copy(alpha = 0.3f) else Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontSize = 18.sp)


            if (isLocked) {
                Text("$count", fontWeight = FontWeight.Bold)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onCountChange?.invoke(count - 1) }, enabled = count > 0) {
                        Text("-")
                    }
                    Text("$count", modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                    IconButton(onClick = { onCountChange?.invoke(count + 1) }) {
                        Text("+")
                    }
                }
            }
        }
    }
}
