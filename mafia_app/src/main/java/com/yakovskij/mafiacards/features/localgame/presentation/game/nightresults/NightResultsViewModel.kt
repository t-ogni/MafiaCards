package com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.GameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults.formatter.NightEventFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class NightResultsViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: GameSettingsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(NightResultsUiState())
    val uiState: State<NightResultsUiState> = _uiState

    private var timerJob: Job? = null

    private val formatter = NightEventFormatter()

    fun startDiscussion(){
        initDiscussionState()
        startTimer()
    }

    fun initDiscussionState() {
        if (!_uiState.value.shouldInit) return
        val dayTime = settingsRepository.getTimerSettings().dayTime
        val results = gameRepository.getEngine().getNightResults()

        _uiState.value = NightResultsUiState(
            nightResults = formatter.format(results),
            dayTimeSeconds = dayTime,
            dayTimeSecondsLeft = dayTime
        )
    }

    fun resetDiscussionState() {
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


    fun stopTimer() {
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel() // Потенциальное утекание памяти
    }
}
