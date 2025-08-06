package com.yakovskij.mafiacards.features.localgame.data.gamesettings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.yakovskij.mafia_engine.domain.GameSettings
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafia_engine.domain.RoleType
import com.yakovskij.mafiacards.features.localgame.domain.TimerSettings

class FakeGameSettingsRepository : IGameSettingsRepository {

    private var gameSettings: GameSettings = GameSettings()
    private var timerSettings: TimerSettings = TimerSettings()

    private val _players = mutableStateOf<List<Player>>(emptyList())
    override val players: State<List<Player>> get() = _players

    override fun getGameSettings(): GameSettings = gameSettings
    override fun getTimerSettings(): TimerSettings = timerSettings

    override suspend fun loadSettings() {
        // Фейковая реализация — ничего не делает
    }

    private fun saveSettings() {
        // Фейковая реализация — ничего не делает
    }

    override suspend fun updateRoleCount(role: RoleType, count: Int) {
        gameSettings.setCount(role, count.coerceAtLeast(0))
        saveSettings()
        syncPlayerList(gameSettings.getTotalRolesCount())
    }

    override suspend fun incrementRoleCount(role: RoleType) {
        updateRoleCount(role, gameSettings.getCountFor(role) + 1)
    }

    override suspend fun decrementRoleCount(role: RoleType) {
        updateRoleCount(role, (gameSettings.getCountFor(role) - 1).coerceAtLeast(0))
    }

    override fun getRoleCount(role: RoleType): Int = gameSettings.getCountFor(role)

    override fun getTotalActiveRolesCount(): Int = gameSettings.getTotalActiveRolesCount()

    override fun getTotalRolesCount(): Int = gameSettings.getTotalRolesCount()

    override fun getDayTime(): Int = timerSettings.dayTime
    override fun getNightTime(): Int = timerSettings.nightTime
    override fun getVoteTime(): Int = timerSettings.voteTime

    override fun updateDayTime(seconds: Int) {
        timerSettings = timerSettings.copy(dayTime = seconds)
    }

    override fun updateNightTime(seconds: Int) {
        timerSettings = timerSettings.copy(nightTime = seconds)
    }

    override fun updateVoteTime(seconds: Int) {
        timerSettings = timerSettings.copy(voteTime = seconds)
    }

    override fun updatePlayerName(index: Int, name: String) {
        val updated = _players.value.toMutableList()
        if (index in updated.indices) {
            updated[index] = updated[index].copy(name = name)
            _players.value = updated
        }
    }

    override fun syncPlayerList(totalPlayers: Int) {
        val current = _players.value
        val newList = List(totalPlayers) { index ->
            current.getOrNull(index) ?: Player(index + 1, "Игрок ${index + 1}")
        }
        _players.value = newList
    }

    override fun balanceRolesToMatchPlayerCount(newPlayerCount: Int): Boolean {
        val currentCount = gameSettings.getTotalRolesCount()
        val civilians = gameSettings.getCountFor(RoleType.CIVILIAN)

        return when {
            newPlayerCount > currentCount -> {
                val toAdd = newPlayerCount - currentCount
                gameSettings.setCount(RoleType.CIVILIAN, civilians + toAdd)
                true
            }
            newPlayerCount < currentCount -> {
                val toRemove = currentCount - newPlayerCount
                if (civilians >= toRemove) {
                    gameSettings.setCount(RoleType.CIVILIAN, civilians - toRemove)
                    true
                } else {
                    false
                }
            }
            else -> true
        }
    }
}
