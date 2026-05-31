package com.yakovskij.mafiacards.features.localgame.presentation.game

import com.yakovskij.mafia_engine.domain.GamePhase
import com.yakovskij.mafia_engine.domain.role.RoleType

data class GameUiState(
    val previousPhase: GamePhase = GamePhase.SETUP,
    val phase: GamePhase = GamePhase.SETUP,
    val hasProceededToNextPhase: Boolean = false,
    val winner: RoleType? = null,
    val error: String? = null,
)
