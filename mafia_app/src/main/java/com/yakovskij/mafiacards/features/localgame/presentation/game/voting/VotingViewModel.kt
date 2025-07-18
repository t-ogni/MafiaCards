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
open class VotingViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: GameSettingsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(VotingUiState())
    open val uiState: State<VotingUiState> = _uiState

    private lateinit var players: List<Player>
    private lateinit var alive: List<Player>

    init {
       initState()
    }

    open fun initState() {
        if (!_uiState.value.shouldInit) return
        players = gameRepository.getState().players
        alive = players.filter { it.isAlive == true }

        _uiState.value = _uiState.value.copy(
            votingCandidates = alive,
            currentPlayer = alive.first(),
            currentIndex = 0
        )
    }

    open fun addVote(performer: Player, target: Player){
        gameRepository.getEngine().user(performer.id).voted(target.id)
    }

    fun selectPlayerToVote(target: Player){
        _uiState.value = _uiState.value.copy(selectedToVotePlayer = target)
    }

    fun nextPlayerOrFinish(){
        val state = _uiState.value
        if (state.currentIndex < alive.lastIndex) {
            // Следующий игрок
            val nextIndex = state.currentIndex + 1
            _uiState.value = state.copy(
                currentIndex = nextIndex,
                currentPlayer = alive[nextIndex],
                selectedToVotePlayer = null
            )
        } else {
            // Все игроки посмотрели карты
            _uiState.value = state.copy(
                isVotingFinished = true
            )
        }
    }

    fun confirmVote() {
        addVote(_uiState.value.currentPlayer, _uiState.value.selectedToVotePlayer!!)
        nextPlayerOrFinish()
    }

    fun skipVote(){
        nextPlayerOrFinish()
    }
}

