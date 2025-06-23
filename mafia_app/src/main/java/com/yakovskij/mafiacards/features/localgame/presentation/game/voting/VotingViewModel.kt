package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.GameSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class VotingViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: GameSettingsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(VotingUiState())
    val uiState: State<VotingUiState> = _uiState

    init {
       initState()
    }

    fun initState() {
        if (!_uiState.value.shouldInit) return
        val players = gameRepository.getState().players
        val alive = players.filter { it.isAlive }

        _uiState.value = _uiState.value.copy(
            votingCandidates = alive
        )
    }

    fun addVote(performer: Player, target: Player){
        gameRepository.getEngine().user(performer.id).voted(target.id)
    }
}

