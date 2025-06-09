package com.yakovskij.mafiacards.features.localgame.presentation.game.night

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.features.game.domain.GamePhase
import com.yakovskij.mafiacards.features.game.domain.Player
import com.yakovskij.mafiacards.features.localgame.presentation.game.GameViewModel

@Composable
fun NightPhaseScreen(
    viewModel: NightActionsViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState

    val player = uiState.currentPlayer ?: return

    if (uiState.isNightFinished)
        onNextPhase()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ночь: Ход игрока ${player.name}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = {
                uiState.timeLeftSeconds / uiState.timeToAction

            },
        )

        Spacer(Modifier.height(8.dp))

        uiState.promptText.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (uiState.availableTargets.isNotEmpty()) {
            Text("Выберите цель:")

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.availableTargets) { target ->
                    Button(
                        onClick = { viewModel.onNightActionSelected(target) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(target.name)
                    }
                }
            }
        } else {
            // Например, для мирного жителя — "действие понарошку"
            Button(
                onClick = { viewModel.onNightActionSelected(null) },
                enabled = !uiState.isActionTaken
            ) {
                Text("Продолжить")
            }
        }
    }
}
