package com.yakovskij.mafia_engine.domain

import com.yakovskij.mafia_engine.domain.role.RoleType

data class GameState(
    var players: List<Player>,
    var currentPhase: GamePhase,
    var cycleNumber: Int,
    var winner: RoleType? = null
)
