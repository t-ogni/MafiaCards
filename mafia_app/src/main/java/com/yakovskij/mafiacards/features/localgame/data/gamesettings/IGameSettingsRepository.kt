package com.yakovskij.mafiacards.features.localgame.data.gamesettings

import androidx.compose.runtime.State
import com.yakovskij.mafia_engine.domain.GameSettings
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafia_engine.domain.role.RoleType
import com.yakovskij.mafiacards.features.localgame.domain.TimerSettings

interface IGameSettingsRepository {
    val players: State<List<Player>>
    suspend fun loadSettings()
    fun getGameSettings(): GameSettings
    fun getTimerSettings(): TimerSettings
    suspend fun updateRoleCount(role: RoleType, count: Int)
    suspend fun incrementRoleCount(role: RoleType)
    suspend fun decrementRoleCount(role: RoleType)
    fun getRoleCount(role: RoleType): Int
    fun getTotalActiveRolesCount(): Int
    fun getTotalRolesCount(): Int
    fun getDayTime(): Int
    fun getNightTime(): Int
    fun getVoteTime(): Int
    fun updateDayTime(seconds: Int)
    fun updateNightTime(seconds: Int)
    fun updateVoteTime(seconds: Int)
    fun updatePlayerName(index: Int, name: String)
    fun syncPlayerList(totalPlayers: Int)
    fun balanceRolesToMatchPlayerCount(newPlayerCount: Int): Boolean
}

