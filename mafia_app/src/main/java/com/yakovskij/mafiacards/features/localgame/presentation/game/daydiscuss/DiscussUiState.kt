package com.yakovskij.mafiacards.features.localgame.presentation.game.daydiscuss

import com.yakovskij.mafia_engine.domain.Player

data class DiscussUiState(
    val shouldInit: Boolean = true,
    val dayNumber: Int = 1,
    val alivePlayers: List<Player> = emptyList(),
    val dayTimeSeconds: Int = 40,
    val dayTimeSecondsLeft: Int = 40,
    val isFinished: Boolean = false
)
