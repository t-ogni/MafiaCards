package com.yakovskij.mafiacards.features.localgame.presentation.setupPlayers

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.GameSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PlayerSetupViewModel @Inject constructor(
    private val repository: GameSettingsRepository
) : ViewModel() {
    val players: State<List<Player>> = repository.players

    fun updateTotalPlayers(count: Int) {
        repository.syncPlayerList(count)
    }

    fun updatePlayerName(index: Int, name: String) {
        repository.updatePlayerName(index, name)
    }
}