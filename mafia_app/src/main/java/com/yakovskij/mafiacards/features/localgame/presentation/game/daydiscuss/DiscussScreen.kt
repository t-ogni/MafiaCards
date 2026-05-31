package com.yakovskij.mafiacards.features.localgame.presentation.game.daydiscuss

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.core.ui.components.StyledButton
import com.yakovskij.mafiacards.core.ui.components.StyledCard
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor

@Composable
fun DiscussScreen(
    viewModel: DiscussViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState

    LaunchedEffect(uiState.shouldInit) {
        if (uiState.shouldInit) viewModel.start()
    }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            viewModel.reset()
            onNextPhase()
        }
    }

    StyledVineBackground()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "День ${uiState.dayNumber}",
                style = MaterialTheme.typography.displaySmall,
                color = LightTextColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Обсудите, кто может быть мафией.",
                style = MaterialTheme.typography.bodyLarge,
                color = LightTextColor.copy(alpha = 0.85f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "В живых (${uiState.alivePlayers.size}):",
                style = MaterialTheme.typography.titleMedium,
                color = LightTextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            StyledCard(modifier = Modifier.weight(1f).fillMaxWidth()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.alivePlayers) { player ->
                        Text(
                            text = player.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = LightTextColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "⏱ ${uiState.dayTimeSecondsLeft} секунд",
                style = MaterialTheme.typography.bodyLarge,
                color = LightTextColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(8.dp))

            StyledButton(
                text = "Перейти к голосованию",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.stopTimer()
                    onNextPhase()
                }
            )
        }
    }
}
