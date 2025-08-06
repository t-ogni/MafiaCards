package com.yakovskij.mafiacards.features.localgame.presentation.setupDeck

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafia_engine.*
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.FakeGameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.GameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.presentation.setupDeck.DeckViewModel
import kotlin.Int


class FakeDeckViewModel : DeckViewModel(
    repository = FakeGameSettingsRepository()
) {
    private val _fakeUiState = mutableStateOf(
        DeckUiState(totalPlayers = 33)
    )

    override val uiState: State<DeckUiState>
        get() = _fakeUiState
}

