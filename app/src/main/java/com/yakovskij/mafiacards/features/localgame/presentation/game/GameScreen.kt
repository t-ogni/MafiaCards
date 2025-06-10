package com.yakovskij.mafiacards.features.localgame.presentation.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.features.game.domain.GamePhase
import com.yakovskij.mafiacards.features.localgame.presentation.game.dealCards.DealCardsScreen
import com.yakovskij.mafiacards.features.localgame.presentation.game.discussion.DayDiscussionScreen
import com.yakovskij.mafiacards.features.localgame.presentation.game.night.NightActionsViewModel
import com.yakovskij.mafiacards.features.localgame.presentation.game.night.NightPhaseScreen
import com.yakovskij.mafiacards.features.localgame.presentation.game.prop.PropScreen


@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    nightActionsViewModel: NightActionsViewModel = hiltViewModel()
) {
    val uiState by gameViewModel.uiState

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState.phase) {
                GamePhase.SETUP -> DealCardsScreen(onGameStart = gameViewModel::advancePhase)
                GamePhase.NIGHT -> NightPhaseScreen(onNextPhase = gameViewModel::advancePhase)
                GamePhase.DAY_DISCUSSION -> DayDiscussionScreen(onNextPhase = gameViewModel::advancePhase)
                else -> PropScreen(onAction = gameViewModel::advancePhase)
            }
        }
    }
}


