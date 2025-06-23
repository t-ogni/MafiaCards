package com.yakovskij.mafiacards.features.localgame.presentation.setupGame

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.GameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.domain.TimerSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetupGameViewModel @Inject constructor(
    private val settingsRepository: GameSettingsRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _totalPlayers = mutableStateOf(settingsRepository.getGameSettings().totalPlayers)
    val totalPlayers: State<Int> = _totalPlayers

    private val _dayTime = mutableStateOf(settingsRepository.getTimerSettings().dayTime)
    val dayTime: State<Int> = _dayTime

    private val _nightTime = mutableStateOf(settingsRepository.getTimerSettings().nightTime)
    val nightTime: State<Int> = _nightTime

    private val _voteTime = mutableStateOf(settingsRepository.getTimerSettings().voteTime)
    val voteTime: State<Int> = _voteTime

    val _players = mutableStateOf<List<Player>>(emptyList())

    fun changeTotalPlayers(value: Int) {
        _totalPlayers.value = value
        settingsRepository.updateTotalPlayers(value)
    }

    fun changeDayTime(value: Int) {
        _dayTime.value = value
        settingsRepository.updateDayTime(value)
    }

    fun changeNightTime(value: Int) {
        _nightTime.value = value
        settingsRepository.updateNightTime(value)
    }

    fun changeVoteTime(value: Int) {
        _voteTime.value = value
        settingsRepository.updateVoteTime(value)
    }

    fun getFinalSettings(): Pair<GameSettings, TimerSettings> {
        return settingsRepository.getGameSettings() to settingsRepository.getTimerSettings()
    }
    fun startGame() {
        if(settingsRepository.getTotalActiveRolesCount() > settingsRepository.getGameSettings().totalPlayers) {
            settingsRepository.updateTotalPlayers(settingsRepository.getTotalActiveRolesCount())
        }

        gameRepository.saveSettings(settingsRepository.getGameSettings())
        gameRepository.startGame(settingsRepository.players.value)
    }

}
