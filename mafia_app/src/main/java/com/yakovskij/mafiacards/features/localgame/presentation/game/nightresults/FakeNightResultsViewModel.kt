package com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.FakeGameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.GameSettingsRepository
import com.yakovskij.mafiacards.features.localgame.presentation.game.voting.VotingUiState
import com.yakovskij.mafiacards.features.localgame.presentation.game.voting.VotingViewModel


class FakeNightResultsViewModel : NightResultsViewModel(
    gameRepository = GameRepository(),
    settingsRepository = FakeGameSettingsRepository()
) {
    private val _fakeUiState = mutableStateOf(
        NightResultsUiState(
            shouldInit = false,
            nightResults = listOf(
                "Игрок Б был убит маньяком",
                "Мафия попыталась напасть на игрока А, но его спас коммисар",
            ),
        )
    )

    override val uiState: State<NightResultsUiState>
        get() = _fakeUiState


}

