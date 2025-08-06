package com.yakovskij.mafiacards.features.localgame.presentation.game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.yakovskij.mafia_engine.*
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.GameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.presentation.game.night.NightActionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private var engine: GameEngine = gameRepository.getEngine()

    private val _uiState = mutableStateOf(GameUiState())
    val uiState: State<GameUiState> = _uiState

    fun advancePhase() {
        engine.advancePhase()
        val newPhase = engine.getSession().state.currentPhase

        _uiState.value = _uiState.value.copy(
            phase = newPhase,
            hasProceededToNextPhase = false
        )
    }

    fun resetGame() {
        gameRepository.reset()
    }

    fun markPhaseAsCompleted() {
        _uiState.value = _uiState.value.copy(hasProceededToNextPhase = true)
    }
}