package com.yakovskij.mafiacards.features.localgame.presentation.game.helloday

import com.yakovskij.mafia_engine.domain.role.TeamSide

data class HellodayUiState(
    val page: Int = 0,
    val teamCounts: Map<TeamSide, Int> = emptyMap()
)