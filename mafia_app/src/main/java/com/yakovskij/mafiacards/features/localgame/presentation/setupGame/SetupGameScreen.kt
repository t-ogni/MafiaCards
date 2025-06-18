package com.yakovskij.mafiacards.features.localgame.presentation.setupGame

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

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
    Scaffold() { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            Text("Настройки игры", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
            SettingRow("Игроки:", totalPlayers, viewModel::changeTotalPlayers)
            SettingRow("Время дня:", dayTime, viewModel::changeDayTime)
            SettingRow("Время ночи:", nightTime, viewModel::changeNightTime)
            SettingRow("Время голосования:", voteTime, viewModel::changeVoteTime)

            Spacer(Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onDeckClick) { Text("Колода") }
                Button(onClick = onUsersClick) { Text("Игроки") }
                Button(onClick = {
                    viewModel.startGame()
                    onStartClick()
                }
                ) { Text("Начать игру") }
            }
        }
    }
}

@Composable
fun SettingRow(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(label, modifier = Modifier.defaultMinSize(150.dp, Dp.Unspecified))
        IconButton(onClick = { if (value > 1) onValueChange(value - 1) }) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Decrease")
        }
        Text("$value", modifier = Modifier.defaultMinSize(30.dp, Dp.Unspecified), textAlign = TextAlign.Center)
        IconButton(onClick = { onValueChange(value + 1) }) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Increase")
        }
    }
}