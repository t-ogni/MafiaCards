package com.yakovskij.mafiacards.features.localgame.presentation.game.voteresults

import com.yakovskij.mafia_engine.domain.VoteResult

data class VoteResultsUiState(
    val shouldInit: Boolean = true,
    val voteResults: String = "Результат голосования не поступал...",
    val showTimeSeconds: Int = 60,
    val showTimeSecondsLeft: Int = 60,
    val isScreenFinished: Boolean = false
)
