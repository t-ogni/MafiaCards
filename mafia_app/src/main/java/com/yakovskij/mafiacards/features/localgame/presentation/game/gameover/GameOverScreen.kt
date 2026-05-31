package com.yakovskij.mafiacards.features.localgame.presentation.game.gameover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafia_engine.domain.role.RoleType
import com.yakovskij.mafia_engine.domain.role.TeamSide
import com.yakovskij.mafiacards.core.ui.components.StyledButton
import com.yakovskij.mafiacards.core.ui.components.StyledCard
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedButton
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor

@Composable
fun GameOverScreen(
    winner: RoleType?,
    players: List<Player>,
    onExit: () -> Unit,
    onNewGame: () -> Unit
) {
    val (title, accent) = when (winner?.side) {
        TeamSide.MAFIA -> "Победила мафия!" to Color(0xFFE63B2C)
        TeamSide.MANIACS -> "Победил маньяк!" to Color(0xFFB14AE6)
        TeamSide.CIVILIANS -> "Победил город!" to Color(0xFF4AC6E6)
        else -> "Игра окончена" to LightTextColor
    }

    StyledVineBackground()
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "ИГРА ОКОНЧЕНА",
                style = MaterialTheme.typography.titleMedium,
                color = LightTextColor.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall,
                color = accent,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Раскрытие ролей",
                style = MaterialTheme.typography.titleMedium,
                color = LightTextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            StyledCard(modifier = Modifier.weight(1f).fillMaxWidth()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(players) { player ->
                        PlayerRoleRow(player)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StyledButton(
                    text = "Заново",
                    modifier = Modifier.weight(1f),
                    onClick = onNewGame
                )
                StyledOutlinedButton(
                    text = "Выйти",
                    modifier = Modifier.weight(1f),
                    onClick = onExit
                )
            }
        }
    }
}

@Composable
private fun PlayerRoleRow(player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (player.isAlive) "🟢" else "🔴",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = player.name,
            style = MaterialTheme.typography.bodyLarge,
            color = LightTextColor,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = player.role?.title ?: "—",
            style = MaterialTheme.typography.bodyMedium,
            color = LightTextColor.copy(alpha = 0.85f)
        )
    }
}
