package com.yakovskij.mafia_engine.domain

data class GameState(
    var players: List<Player>,
    var currentPhase: GamePhase,
    var turnNumber: Int,
    var winner: RoleType? = null
)
