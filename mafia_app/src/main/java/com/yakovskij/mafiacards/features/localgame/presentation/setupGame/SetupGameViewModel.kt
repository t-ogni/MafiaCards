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
        val success = settingsRepository.balanceRolesToMatchPlayerCount(value)

        if (!success) {
            _uiState.value = _uiState.value.copy(
                errors = uiState.value.errors + "Невозможно уменьшить число игроков — недостаточно мирных для удаления!"
            )
            return
        }

        _totalPlayers.intValue = value
        settingsRepository.syncPlayerList(value)
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

    fun startGame(): Boolean {
        val players = settingsRepository.players.value
        val totalPlayers = players.size
        val totalRoles = settingsRepository.getTotalRolesCount()

        if (totalRoles != totalPlayers) {
            _uiState.value = _uiState.value.copy(
                errors = uiState.value.errors + "Нельзя начать игру — число ролей ($totalRoles) не совпадает с числом игроков ($totalPlayers)"
            )
            return false
        }

        if (totalPlayers < 3) {
            _uiState.value = _uiState.value.copy(
                errors = uiState.value.errors + "Для игры нужно минимум 3 игрока"
            )
            return false
        }

        if (players.any { it.name.isBlank() }) {
            _uiState.value = _uiState.value.copy(
                errors = uiState.value.errors + "У всех игроков должно быть имя"
            )
            return false
        }

        return try {
            gameRepository.saveSettings(settingsRepository.getGameSettings())
            gameRepository.startGame(players)
            true
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errors = uiState.value.errors + (e.message ?: "Не удалось начать игру")
            )
            false
        }
    }

    fun consumeError() {
        if (_uiState.value.errors.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(errors = _uiState.value.errors.drop(1))
        }
    }
}
