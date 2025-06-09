package com.yakovskij.mafiacards.features.localgame.presentation.game

import com.yakovskij.mafiacards.features.game.domain.GamePhase
import com.yakovskij.mafiacards.features.game.domain.Player
import com.yakovskij.mafiacards.features.game.domain.RoleType

data class GameUiState(
    val previousPhase: GamePhase = GamePhase.SETUP,
    val phase: GamePhase = GamePhase.SETUP,
    val error: String? = null,
)

