package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import com.yakovskij.mafiacards.features.game.domain.Player

data class VotingUiState(
    val votingCandidates: List<Player> = emptyList(),
    val votes: Map<Player, Player> = emptyMap(), // Кто за кого
    val votedPlayer: Player? = null,
    val isVotingFinished: Boolean = false
)
