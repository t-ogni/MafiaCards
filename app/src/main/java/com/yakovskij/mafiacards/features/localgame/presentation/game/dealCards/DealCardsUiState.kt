package com.yakovskij.mafiacards.features.localgame.presentation.game.dealCards

import com.yakovskij.mafiacards.features.game.domain.Player

data class DealCardsUiState(
    val currentIndex: Int = 0,
    val currentPlayer: Player? = null,
    val isCardFlipped: Boolean = false,
    val allCardsDealt: Boolean = false,
    val totalPlayers: Int = 0
)