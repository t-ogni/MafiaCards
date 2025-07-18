package com.yakovskij.mafiacards.features.localgame.presentation.game.voteresults

data class VoteResultsUiState(
    val shouldInit: Boolean = true,
    val nightResults: List<String> = emptyList(),
    val dayTimeSeconds: Int = 60,
    val dayTimeSecondsLeft: Int = 60,
    val isViewingEnded: Boolean = false
)
