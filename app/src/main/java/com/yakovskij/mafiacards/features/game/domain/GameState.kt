package com.yakovskij.mafiacards.features.game.domain

data class GameState(
    var players: List<Player>,
    var deck: List<RoleCard>,
    var currentPhase: GamePhase,
    var turnNumber: Int,
    var winner: RoleType? = null

)
