package com.yakovskij.mafiacards.features.localgame.presentation.game.night

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.core.ui.components.StyledButton
import com.yakovskij.mafiacards.core.ui.components.StyledCard
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LinearProgressIndicator(
                progress = {
                    if (uiState.timeToAction > 0) {
                        (uiState.timeLeftSeconds / uiState.timeToAction).coerceIn(0f, 1f)
                    } else 1f
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

        // Приватный результат проверки детектива.
        uiState.revealText?.let { reveal ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                StyledCard(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Результат проверки",
                        style = MaterialTheme.typography.titleMedium,
                        color = LightTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    Text(
                        text = reveal,
                        style = MaterialTheme.typography.headlineSmall,
                        color = LightTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )
                    StyledButton(
                        text = "Передать дальше",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            flipped = false
                            viewModel.dismissReveal()
                        }
                    )
                }
            }
        }
    }
}
