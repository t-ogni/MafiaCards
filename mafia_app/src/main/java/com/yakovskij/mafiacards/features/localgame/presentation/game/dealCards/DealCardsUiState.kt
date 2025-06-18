package com.yakovskij.mafiacards.features.localgame.presentation.dealcards

import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafiacards.features.localgame.presentation.game.night.components.PlayerCard

data class DealCardsUiState(
    val currentIndex: Int = 0,
    val currentPlayer: Player = Player(-999, "Player"),
    val isCardFlipped: Boolean = false,
    val allCardsDealt: Boolean = false,
    val totalPlayers: Int = 0
)