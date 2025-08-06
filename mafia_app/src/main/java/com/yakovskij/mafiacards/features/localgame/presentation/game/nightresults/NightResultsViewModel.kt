package com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakovskij.mafia_engine.presentation.NightFormatter
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.GameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.IGameSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
open class NightResultsViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: IGameSettingsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(NightResultsUiState())
    open val uiState: State<NightResultsUiState> = _uiState

    private var timerJob: Job? = null

    private val formatter = NightFormatter()

    open fun startDiscussion(){
        initDiscussionState()
        startTimer()
    }

    open fun initDiscussionState() {
        if (!_uiState.value.shouldInit) return
        val dayTime = settingsRepository.getDayTime()
        val results = gameRepository.getEngine().getNightResults()

        _uiState.value = NightResultsUiState(
            nightResults = formatter.format(results),
            dayTimeSeconds = dayTime,
            dayTimeSecondsLeft = dayTime
        )
    }

    open fun resetDiscussionState() {
        _uiState.value = NightResultsUiState()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.dayTimeSecondsLeft > 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    dayTimeSecondsLeft = _uiState.value.dayTimeSecondsLeft - 1
                )
            }

            _uiState.value = _uiState.value.copy(isViewingEnded = true) // <- таймер завершён
        }
    }


    open fun stopTimer() {
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel() // Потенциальное утекание памяти
    }
}
