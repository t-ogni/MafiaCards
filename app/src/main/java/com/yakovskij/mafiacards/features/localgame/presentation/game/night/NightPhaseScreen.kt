package com.yakovskij.mafiacards.features.localgame.presentation.game.night

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.features.localgame.presentation.game.night.components.PlayerCard

@Composable
fun NightPhaseScreen(
    viewModel: NightActionsViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState
    var flipped by remember { mutableStateOf(false) }

    // Завершение ночи, возвращаем экран к первоначальному виду, к готовности прочитать gameState
    LaunchedEffect(uiState.isNightFinished) {
        if (uiState.isNightFinished) {
            viewModel.resetNightState()
            onNextPhase()
        }
    }

    // Старт ночи один раз.
    LaunchedEffect(uiState.shouldInit) {
        if (uiState.shouldInit) {
            viewModel.startNight()
        }
    }

    LaunchedEffect(uiState.isTimeExpired) {
        if (uiState.isTimeExpired) {
            flipped = !flipped
        }
    }


    val player = uiState.currentPlayer ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LinearProgressIndicator(
            progress = {
                (uiState.timeLeftSeconds / uiState.timeToAction).coerceIn(0f, 1f)
            },
        )

        Spacer(Modifier.height(30.dp))

        PlayerCard(
            player = player,
            promptText = uiState.promptText,
            availableTargets = uiState.availableTargets,
            isActionTaken = uiState.isActionTaken,
            onActionSelected = {
                viewModel.onNightActionSelected(it)
                flipped = !flipped
            },
            isFrontShown = flipped,
            onBackClicked = {
                flipped = !flipped
                viewModel.startNightTimer()
            }
        )
    }
}

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.spacedBy(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Ночь: Ход игрока ${player.name}",
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Spacer(Modifier.height(8.dp))
//
//
//        Text(
//            text = uiState.promptText,
//            style = MaterialTheme.typography.bodyLarge
//        )
//
//        if (uiState.availableTargets.isNotEmpty()) {
//            Text("Выберите цель:")
//
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                items(uiState.availableTargets) { target ->
//                    Button(
//                        onClick = { viewModel.onNightActionSelected(target) },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(target.name)
//                    }
//                }
//            }
//        } else {
//            // Например, для мирного жителя — "действие понарошку"
//            Button(
//                onClick = { viewModel.onNightActionSelected(null) },
//                enabled = !uiState.isActionTaken
//            ) {
//                Text("Продолжить")
//            }
//        }
//    }
//}

