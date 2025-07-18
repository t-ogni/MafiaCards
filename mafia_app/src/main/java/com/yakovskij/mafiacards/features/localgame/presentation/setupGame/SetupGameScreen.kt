package com.yakovskij.mafiacards.features.localgame.presentation.setupGame

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.core.ui.components.StyledButton
import com.yakovskij.mafiacards.core.ui.components.StyledHeadlineMedium
import com.yakovskij.mafiacards.core.ui.components.StyledIconButtonFilled
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedCard
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground

@Composable
fun SetupGameScreen(
    viewModel: SetupGameViewModel = hiltViewModel(),
    onStartClick: () -> Unit,
    onUsersClick: () -> Unit,
    onDeckClick: () -> Unit
) {
    val totalPlayers by viewModel.totalPlayers
    val dayTime by viewModel.dayTime
    val nightTime by viewModel.nightTime
    val voteTime by viewModel.voteTime

    StyledVineBackground()
        Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(12.dp)) {
            Spacer(Modifier.height(24.dp))
            StyledHeadlineMedium("Настройки")
            Spacer(Modifier.height(16.dp))
            Column (verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SettingRow("Игроки:", totalPlayers, viewModel::changeTotalPlayers)
                SettingRow("Время дня:", dayTime, viewModel::changeDayTime)
                SettingRow("Время ночи:", nightTime, viewModel::changeNightTime)
                SettingRow("Время голосования:", voteTime, viewModel::changeVoteTime)

            }

            Spacer(Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                StyledButton(
                    text = "Колода",
                    modifier = Modifier.weight(1f),
                    onClick = onDeckClick
                )
                StyledButton(
                    text = "Игроки",
                    modifier = Modifier.weight(1f),
                    onClick = onUsersClick
                )
            }

            Spacer(Modifier.height(24.dp))

            Row {
                StyledButton(
                    text = "Начать игру",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.startGame()
                        onStartClick()
                    }
                )
            }

        }
}

@Composable
fun SettingRow(label: String, value: Int, onValueChange: (Int) -> Unit) {

    StyledOutlinedCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(label, modifier = Modifier.defaultMinSize(150.dp, Dp.Unspecified).weight(1f))
            StyledIconButtonFilled(
                icon = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Decrease",
                onClick = { if (value > 1) onValueChange(value - 1) })
            Text(
                "$value",
                modifier = Modifier.defaultMinSize(30.dp, Dp.Unspecified),
                textAlign = TextAlign.Center
            )
            StyledIconButtonFilled(
                icon = Icons.Default.KeyboardArrowRight,
                contentDescription = "Increase",
                onClick = { onValueChange(value + 1) })
        }
    }
}