package com.yakovskij.mafia_engine.domain

sealed class VoteResult {
    data class PlayerJailed(val player: Player) : VoteResult()
    data class Tie(val tiedPlayers: List<Player>) : VoteResult()
}
