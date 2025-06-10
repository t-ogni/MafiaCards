package com.yakovskij.mafiacards.features.localgame.presentation.game.discussion

import com.yakovskij.mafiacards.features.game.domain.NightAction
import com.yakovskij.mafiacards.features.game.domain.Player

data class DayDiscussionUiState(
    val shouldInit: Boolean = true,
    val nightResults: List<String> = emptyList(),
    val dayTimeSeconds: Int = 60,
    val dayTimeSecondsLeft: Int = 60,
    val isViewingEnded: Boolean = false
)
