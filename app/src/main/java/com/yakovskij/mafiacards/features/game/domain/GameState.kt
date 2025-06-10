package com.yakovskij.mafiacards.features.game.domain

data class GameState(
    var players: List<Player>,
    var currentPhase: GamePhase,
    var turnNumber: Int,
    var winner: RoleType? = null

)
