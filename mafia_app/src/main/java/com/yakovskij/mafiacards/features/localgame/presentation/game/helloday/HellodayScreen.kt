package com.yakovskij.mafiacards.features.localgame.presentation.game.helloday

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafia_engine.domain.role.TeamSide
import com.yakovskij.mafiacards.core.ui.components.StyledButton
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground

@Composable
fun HellodayScreen(
    viewModel: HellodayViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState
    val teamCounts = uiState.teamCounts

    StyledVineBackground()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Добро пожаловать в город!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Сегодня первый день. Познакомьтесь, осмотритесь —\nсегодня голосования не будет. С наступлением ночи начнётся игра.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "В игре участвуют:",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(Modifier.height(8.dp))

            teamCounts.forEach { (side, count) ->
                TeamCountRow(side, count)
            }
        }

        StyledButton(
            text = "Город засыпает",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            onClick = onNextPhase
        )
    }
}

@Composable
fun TeamCountRow(side: TeamSide, count: Int) {
    val (emoji, label, color) = when (side) {
        TeamSide.CIVILIANS -> Triple("🔵", "Мирные", Color.Cyan)
        TeamSide.MAFIA -> Triple("🔴", "Мафия", Color.Red)
        TeamSide.MANIACS -> Triple("🟣", "Маньяки", Color.Magenta)
        TeamSide.NEUTRAL -> Triple("⚪", "Нейтральные", Color.Gray)
    }
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$emoji $label — $count", color = color, style = MaterialTheme.typography.bodyLarge)
    }
}
