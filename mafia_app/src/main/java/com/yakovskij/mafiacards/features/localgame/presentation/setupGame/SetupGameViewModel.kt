package com.yakovskij.mafiacards.features.localgame.presentation.setupGame

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.GameSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetupGameViewModel @Inject constructor(
    private val settingsRepository: GameSettingsRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _totalPlayers = mutableIntStateOf(settingsRepository.getTotalRolesCount())
    val totalPlayers: State<Int> = _totalPlayers

    private val _dayTime = mutableIntStateOf(settingsRepository.getDayTime())
    val dayTime: State<Int> = _dayTime

    private val _nightTime = mutableIntStateOf(settingsRepository.getNightTime())
    val nightTime: State<Int> = _nightTime

    private val _voteTime = mutableIntStateOf(settingsRepository.getVoteTime())
    val voteTime: State<Int> = _voteTime

    private val _uiState = mutableStateOf(SetupGameUiState())
    val uiState: State<SetupGameUiState> = _uiState

    fun changeTotalPlayers(value: Int) {
        _totalPlayers.intValue = value
        settingsRepository.up(value)
    }

    fun changeDayTime(value: Int) {
        _dayTime.intValue = value
        settingsRepository.updateDayTime(value)
    }

    fun changeNightTime(value: Int) {
        _nightTime.intValue = value
        settingsRepository.updateNightTime(value)
    }

    fun changeVoteTime(value: Int) {
        _voteTime.intValue = value
        settingsRepository.updateVoteTime(value)
    }

    fun startGame() {
        if(settingsRepository.getTotalActiveRolesCount() > settingsRepository.getGameSettings().totalPlayers()) {
            val errorsNew = uiState.value.errors + "Test"
            _uiState.value = _uiState.value.copy(errors = errorsNew)
        }

        gameRepository.saveSettings(settingsRepository.getGameSettings())
        gameRepository.startGame(settingsRepository.players.value)
    }

}
