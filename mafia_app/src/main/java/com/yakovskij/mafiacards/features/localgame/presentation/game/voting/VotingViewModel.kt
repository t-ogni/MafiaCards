package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import com.yakovskij.mafiacards.features.localgame.data.gamesettings.IGameSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
open class VotingViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: IGameSettingsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(VotingUiState())
    open val uiState: State<VotingUiState> = _uiState

    private var players: List<Player> = emptyList()
    private var alive: List<Player> = emptyList()

    init {
       initState()
    }

    open fun initState() {
        if (!_uiState.value.shouldInit) return
        players = gameRepository.getState().players
        alive = players.filter { it.isAlive }

        if (alive.isEmpty()) return

        _uiState.value = _uiState.value.copy(
            votingCandidates = alive,
            currentPlayer = alive.first(),
            currentIndex = 0,
            votes = emptyMap(),
            shouldInit = false
        )
    }

    fun clearState() {
        _uiState.value = VotingUiState()
    }

    open fun addVote(performer: Player, target: Player){
        gameRepository.getEngine().player(performer.id).voted(target.id)
        _uiState.value = _uiState.value.copy(
            votes = _uiState.value.votes + (performer to target)
        )
    }

    fun updateVote(performerId: Int, targetId: Int){
        val playerController = gameRepository.getEngine().player(performerId)
        playerController.clearVote()
        playerController.voted(targetId)

        // Синхронизируем отображаемую карту голосов.
        val voter = alive.find { it.id == performerId }
        val target = alive.find { it.id == targetId }
        if (voter != null && target != null) {
            _uiState.value = _uiState.value.copy(
                votes = _uiState.value.votes + (voter to target)
            )
        }
    }

    fun selectPlayerToVote(target: Player){
        _uiState.value = _uiState.value.copy(selectedToVotePlayer = target)
    }

    fun getPlayerById(id: Int?, list: List<Player>): Player? {
        return list.find { it.id == id }
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
            // Все игроки проголосовали
            _uiState.value = state.copy(
                isVotingFinished = true
            )
        }
    }

    fun confirmVote() {
        val target = _uiState.value.selectedToVotePlayer ?: return
        addVote(_uiState.value.currentPlayer, target)
        nextPlayerOrFinish()
    }

    fun skipVote(){
        nextPlayerOrFinish()
    }

    fun calculateVotes(){
        gameRepository.getEngine().calculateVotesAndJail()
    }
}
