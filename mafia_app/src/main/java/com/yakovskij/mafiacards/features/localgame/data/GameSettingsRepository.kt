package com.yakovskij.mafiacards.features.localgame.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafiacards.features.localgame.domain.TimerSettings

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameSettingsRepository @Inject constructor() {

    private var gameSettings: GameSettings = GameSettings()
    private var timerSettings: TimerSettings = TimerSettings()

    private val _players = mutableStateOf<List<Player>>(emptyList())
    val players: State<List<Player>> get() = _players

    init {
        syncPlayerList(gameSettings.totalPlayers)
    }

    fun getGameSettings(): GameSettings = gameSettings.copy()
    fun getTimerSettings(): TimerSettings = timerSettings.copy()

    fun updateTotalPlayers(count: Int) {
        gameSettings = gameSettings.copy(totalPlayers = count)
        syncPlayerList(count)
    }

    fun updateMafiaCount(count: Int) {
        gameSettings = gameSettings.copy(mafiaCount = count.coerceAtLeast(0))
    }

    fun updateDoctorCount(count: Int) {
        gameSettings = gameSettings.copy(doctorCount = count.coerceAtLeast(0))
    }

    fun updateDetectiveCount(count: Int) {
        gameSettings = gameSettings.copy(detectiveCount = count.coerceAtLeast(0))
    }

    fun getMafiaCount() = gameSettings.mafiaCount
    fun getDoctorCount() = gameSettings.doctorCount
    fun getDetectiveCount() = gameSettings.detectiveCount

    fun getTotalActiveRolesCount(): Int {
        return (
                gameSettings.mafiaCount +
                gameSettings.doctorCount +
                gameSettings.mafiaCount
                )
    }

    fun updateDayTime(seconds: Int) {
        timerSettings = timerSettings.copy(dayTime = seconds)
    }

    fun updateNightTime(seconds: Int) {
        timerSettings = timerSettings.copy(nightTime = seconds)
    }

    fun updateVoteTime(seconds: Int) {
        timerSettings = timerSettings.copy(voteTime = seconds)
    }

    fun updatePlayerName(index: Int, name: String) {
        val updated = _players.value.toMutableList()
        if (index in updated.indices) {
            updated[index] = updated[index].copy(name = name)
            _players.value = updated
        }
    }

    private fun syncPlayerList(totalPlayers: Int) {
        val current = _players.value
        val newList = List(totalPlayers) { index ->
            current.getOrNull(index) ?: Player(index + 1, "Игрок ${index + 1}")
        }
        _players.value = newList
    }
}