package com.yakovskij.mafiacards.features.localgame.presentation.game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.yakovskij.mafiacards.features.game.data.GameEngine
import com.yakovskij.mafiacards.features.game.data.GameRepository
import com.yakovskij.mafiacards.features.game.domain.GamePhase
import com.yakovskij.mafiacards.features.localgame.data.GameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.presentation.game.night.NightActionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: GameSettingsRepository
) : ViewModel() {

    private var engine: GameEngine = gameRepository.engine!!

    private val _uiState = mutableStateOf(GameUiState())
    val uiState: State<GameUiState> = _uiState

    fun advancePhase() {
        engine.advancePhase()
        val newPhase = engine.getSession().state.currentPhase

        _uiState.value = _uiState.value.copy(
            phase = newPhase
        )
    }

    fun refreshUI() {
        _uiState.value = _uiState.value.copy(phase = gameRepository.getState()?.currentPhase ?: GamePhase.SETUP)
    }
}