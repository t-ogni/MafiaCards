package com.yakovskij.mafiacards.features.localgame.presentation.game.daydiscuss

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.IGameSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscussViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: IGameSettingsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(DiscussUiState())
    val uiState: State<DiscussUiState> = _uiState

    private var timerJob: Job? = null

    fun start() {
        if (!_uiState.value.shouldInit) return
        val dayTime = settingsRepository.getDayTime()
        val state = gameRepository.getState()

        _uiState.value = DiscussUiState(
            shouldInit = false,
            dayNumber = state.cycleNumber,
            alivePlayers = state.players.filter { it.isAlive },
            dayTimeSeconds = dayTime,
            dayTimeSecondsLeft = dayTime
        )

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.dayTimeSecondsLeft > 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    dayTimeSecondsLeft = _uiState.value.dayTimeSecondsLeft - 1
                )
            }
            _uiState.value = _uiState.value.copy(isFinished = true)
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun reset() {
        timerJob?.cancel()
        _uiState.value = DiscussUiState()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
