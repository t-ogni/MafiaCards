package com.yakovskij.mafia_engine.domain

data class GameSettings(
    var totalPlayers: Int = 8,
    var mafiaCount: Int = 2,
    var doctorCount: Int = 1,
    var detectiveCount: Int = 1
)
