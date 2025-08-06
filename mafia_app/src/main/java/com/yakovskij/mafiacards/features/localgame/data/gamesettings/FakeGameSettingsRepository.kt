package com.yakovskij.mafiacards.features.localgame.data.gamesettings

import com.yakovskij.mafia_engine.domain.GameSettings
import com.yakovskij.mafia_engine.domain.RoleType

class FakeGameSettingsRepository : IGameSettingsRepository {

    private var gameSettings = GameSettings(
        roleCounts = mutableMapOf(
            RoleType.MAFIA to 2,
            RoleType.DETECTIVE to 1,
            RoleType.DOCTOR to 1,
            RoleType.CIVILIAN to 4
        )
    )

    fun getGameSettings(): GameSettings = gameSettings.copy()

    fun getRoleCount(role: RoleType): Int {
        return gameSettings.getCountFor(role)
    }

    suspend fun updateRoleCount(role: RoleType, count: Int) {
        gameSettings.setCount(role, count)
    }
}
