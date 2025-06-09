package com.yakovskij.mafiacards.features.localgame.presentation.setupPlayers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.features.game.domain.Player
import com.yakovskij.mafiacards.features.localgame.presentation.setupGame.SetupGameViewModel

@Composable
fun PlayerSetupScreen(
    viewModel: SetupGameViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val totalPlayers by viewModel.totalPlayers
    val players by viewModel.players

    LaunchedEffect(totalPlayers) {
        viewModel.syncPlayerList()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text("Настройка игроков", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            players.forEachIndexed { index, player ->
                OutlinedTextField(
                    value = player.name,
                    onValueChange = { viewModel.updatePlayerName(index, it) },
                    label = { Text("Игрок ${index + 1}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = players.all { it.name.isNotBlank() }
            ) {
                Text("Вернуться")
            }
        }
    }
}


// ViewModel дополнения
val SetupGameViewModel.players: MutableState<List<Player>>
    get() = _players

fun SetupGameViewModel.syncPlayerList() {
    val current = players.value
    val newList = List(totalPlayers.value) { index ->
        current.getOrNull(index) ?: Player(index + 1, "")
    }
    players.value = newList
}

fun SetupGameViewModel.updatePlayerName(index: Int, name: String) {
    val updated = players.value.toMutableList()
    updated[index] = updated[index].copy(name = name)
    players.value = updated
}