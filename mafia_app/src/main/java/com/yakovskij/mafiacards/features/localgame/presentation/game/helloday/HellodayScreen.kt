package com.yakovskij.mafiacards.features.localgame.presentation.game.helloday

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafia_engine.domain.role.TeamSide
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground
import com.yakovskij.mafiacards.features.localgame.presentation.game.night.NightActionsViewModel
@Composable
fun HellodayScreen(
    viewModel: HellodayViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState
    val teamCounts = uiState.teamCounts // Map<TeamSide, Int>

    StyledVineBackground()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Добро пожаловать в город!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "В игре участвуют:",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(Modifier.height(8.dp))

            teamCounts.forEach { (side, count) ->
                TeamCountRow(side, count)
            }
        }
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
