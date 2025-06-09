package com.yakovskij.mafiacards.features.localgame.presentation.game.discussion

import com.yakovskij.mafiacards.features.game.domain.Player

data class DayDiscussionUiState(
    val speakersQueue: List<Player> = emptyList(),
    val currentSpeaker: Player? = null,
    val isDiscussionFinished: Boolean = false
)
