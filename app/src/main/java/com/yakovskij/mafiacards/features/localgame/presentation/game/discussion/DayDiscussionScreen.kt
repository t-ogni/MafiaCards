package com.yakovskij.mafiacards.features.localgame.presentation.game.discussion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.features.game.domain.NightResult

@Composable
fun DayDiscussionScreen(
    viewModel: DayDiscussionViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState

    LaunchedEffect(uiState.shouldInit) {
        if (uiState.shouldInit) {
            viewModel.startDiscussion()
        }
    }

    LaunchedEffect(uiState.isViewingEnded) {
        if (uiState.isViewingEnded) {
            viewModel.resetDiscussionState()
            onNextPhase()
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Результаты ночи:", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            if (uiState.nightResults.isEmpty()) {
                Text("Ночь прошла спокойно.")
            } else {
                uiState.nightResults.forEach { line ->
                    Text("• $line", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Осталось времени: ${uiState.dayTimeSecondsLeft} сек",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.stopTimer()
                    onNextPhase()
                },
                enabled = uiState.dayTimeSecondsLeft > 0
            ) {
                Text("Следующая фаза")
            }
        }
    }
}
