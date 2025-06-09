package com.yakovskij.mafiacards.features.localgame.presentation.setupDeck

data class DeckUiState(
    val mafiaCount: Int = 1,
    val doctorCount: Int = 1,
    val detectiveCount: Int = 1,
    val totalPlayers: Int = 4,
    val errorMessage: String? = null
) {
    val specialRolesCount: Int get() = mafiaCount + doctorCount + detectiveCount
    val civiliansCount: Int get() = (totalPlayers - specialRolesCount).coerceAtLeast(0)
}
