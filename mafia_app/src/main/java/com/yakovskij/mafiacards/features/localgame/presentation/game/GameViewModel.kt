package com.yakovskij.mafiacards.features.localgame.presentation.game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yakovskij.mafia_engine.*
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private var engine: GameEngine = gameRepository.getEngine()

    private val _uiState = mutableStateOf(
        GameUiState(phase = engine.getSession().state.currentPhase)
    )
    val uiState: State<GameUiState> = _uiState

    fun advancePhase() {
        engine.advancePhase()
        val state = engine.getSession().state

        _uiState.value = _uiState.value.copy(
            phase = state.currentPhase,
            winner = state.winner,
            hasProceededToNextPhase = false
        )
    }

    fun getPlayers(): List<Player> = engine.getPlayers()

    fun resetGame() {
        gameRepository.reset()
    }

    fun startNewGame() {
        // Перезапуск с теми же игроками и настройками: создаём свежих живых
        // игроков (без ролей) и заново раздаём колоду.
        val freshPlayers = engine.getPlayers().map { Player(id = it.id, name = it.name) }
        gameRepository.startGame(freshPlayers)
        engine = gameRepository.getEngine()
        _uiState.value = GameUiState(phase = engine.getSession().state.currentPhase)
    }

    fun markPhaseAsCompleted() {
        _uiState.value = _uiState.value.copy(hasProceededToNextPhase = true)
    }
}
