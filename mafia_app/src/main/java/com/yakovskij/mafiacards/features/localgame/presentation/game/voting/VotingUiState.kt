package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import com.yakovskij.mafia_engine.domain.Player


data class VotingUiState(
    val shouldInit: Boolean = true,
    val currentIndex: Int = 0,
    val currentPlayer: Player = Player(-999, "Unknown"),
    val votingCandidates: List<Player> = emptyList(),
    val votes: Map<Player, Player> = emptyMap(),
    val selectedToVotePlayer: Player? = null,
    val isVotingFinished: Boolean = false
)
