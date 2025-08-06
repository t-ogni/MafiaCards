package com.yakovskij.mafiacards.features.localgame.presentation.game.voteresults

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakovskij.mafia_engine.presentation.VoteFormatter
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.GameSettingsRepository
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

    private val voteFormatter = VoteFormatter()
    private var timerJob: Job? = null


    open fun initViewModel(){
        loadVoteResults()
        startTimer()
    }

    open fun loadVoteResults() {
        if (!_uiState.value.shouldInit) return
        val dayTime = settingsRepository.getTimerSettings().dayTime
        val results = gameRepository.getEngine().getVoteResults()


        _uiState.value = VoteResultsUiState(
            showTimeSeconds = dayTime,
            showTimeSecondsLeft = dayTime,
            voteResults = voteFormatter.format(results)
        )
    }

    open fun resetState() {
        _uiState.value = VoteResultsUiState()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.showTimeSecondsLeft > 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    showTimeSecondsLeft = _uiState.value.showTimeSecondsLeft - 1
                )
            }

            _uiState.value = _uiState.value.copy(isScreenFinished = true) // <- таймер завершён
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
