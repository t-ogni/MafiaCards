package com.yakovskij.mafiacards.features.localgame.presentation.game

import com.yakovskij.mafia_engine.domain.GamePhase

data class GameUiState(
    val previousPhase: GamePhase = GamePhase.SETUP,
    val phase: GamePhase = GamePhase.SETUP,
    val hasProceededToNextPhase: Boolean = false,
    val error: String? = null,
)

