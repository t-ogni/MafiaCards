package com.yakovskij.mafiacards.features.localgame.presentation.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafia_engine.domain.GamePhase
import com.yakovskij.mafiacards.features.localgame.presentation.dealcards.DealCardsScreen
import com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults.NightResultsScreen
import com.yakovskij.mafiacards.features.localgame.presentation.game.night.NightPhaseScreen
import com.yakovskij.mafiacards.features.localgame.presentation.game.prop.PropScreen
import com.yakovskij.mafiacards.features.localgame.presentation.game.voteresults.VoteResultsScreen
import com.yakovskij.mafiacards.features.localgame.presentation.game.voting.VotingScreen

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    onExitConfirmed: () -> Unit // <- будет передано из navController
) {
    val uiState by gameViewModel.uiState
    var showExitDialog by remember { mutableStateOf(false) }

    // Перехват кнопки "Назад"
    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Выход из игры") },
            text = { Text("Вы уверены, что хотите выйти в меню? Прогресс будет утерян.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    gameViewModel.resetGame() // очищаем сессию
                    onExitConfirmed() // навигация назад
                }) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showExitDialog = false
                }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState.phase) {
                GamePhase.SETUP -> DealCardsScreen(onNightStart = gameViewModel::advancePhase)
                GamePhase.NIGHT -> NightPhaseScreen(onNextPhase = gameViewModel::advancePhase)
                GamePhase.NIGHT_ENDED -> NightResultsScreen(onNextPhase = gameViewModel::advancePhase)
                GamePhase.DAY_DISCUSSION -> PropScreen(onAction = gameViewModel::advancePhase)
                GamePhase.VOTING -> VotingScreen(onNextPhase = gameViewModel::advancePhase)
                GamePhase.VOTING_ENDED -> VoteResultsScreen(onNextPhase = gameViewModel::advancePhase)
                else -> PropScreen(onAction = gameViewModel::advancePhase)
            }
        }
    }
}



