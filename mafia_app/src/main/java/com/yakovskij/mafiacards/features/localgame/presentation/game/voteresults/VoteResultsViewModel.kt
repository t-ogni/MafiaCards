package com.yakovskij.mafiacards.features.localgame.presentation.game.voteresults

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.GameSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
open class VoteResultsViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: GameSettingsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(VoteResultsUiState())
    open val uiState: State<VoteResultsUiState> = _uiState

    private var timerJob: Job? = null


    open fun startDiscussion(){
        initDiscussionState()
        startTimer()
    }

    open fun initDiscussionState() {
        if (!_uiState.value.shouldInit) return
        val dayTime = settingsRepository.getTimerSettings().dayTime
        val results = gameRepository.getEngine().getNightResults()

        _uiState.value = VoteResultsUiState(
            dayTimeSeconds = dayTime,
            dayTimeSecondsLeft = dayTime
        )
    }

    open fun resetDiscussionState() {
        _uiState.value = VoteResultsUiState()
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
