package com.yakovskij.mafiacards.features.localgame.presentation.game.night

import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafia_engine.domain.role.RoleType

data class NightUiState(
    val currentPlayer: Player? = null,
    val role: RoleType? = null,
    val promptText: String = "",
    val availableTargets: List<Player> = emptyList(),
    val isActionTaken: Boolean = false,
    val isNightFinished: Boolean = false,
    val timeLeftSeconds: Float = 0f,
    val isTimeExpired: Boolean = false,
    val timeToAction: Int = 0,
    val shouldInit: Boolean = true,
    val nightPlayersQueue: List<Player> = emptyList(),
    val nightIndex: Int = 0,
)
