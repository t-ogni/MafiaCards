package com.yakovskij.mafiacards.features.localgame.presentation.dealcards

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlin.collections.firstOrNull
import kotlin.collections.lastIndex

@HiltViewModel
class DealCardsViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(DealCardsUiState())
    val uiState: State<DealCardsUiState> = _uiState

    private val players: List<Player> = gameRepository.getState().players

    init {
        _uiState.value = DealCardsUiState(
            currentPlayer = players.first(),
            totalPlayers = players.size,
            currentIndex = 0,
        )
    }

    fun nextPlayer() {
        val state = _uiState.value
        if (state.currentIndex < players.lastIndex) {
            // Следующий игрок
            val nextIndex = state.currentIndex + 1
            _uiState.value = state.copy(
                currentIndex = nextIndex,
                currentPlayer = players[nextIndex],
                isCardFlipped = false
            )
        } else {
            // Все игроки посмотрели карты
            _uiState.value = state.copy(
                isCardFlipped = true,
                allCardsDealt = true
            )
        }
    }


    fun flipCard() {
        val state = _uiState.value
        _uiState.value = state.copy(isCardFlipped = !state.isCardFlipped)
    }

    fun proceedToNight() {
        gameRepository.getEngine().advancePhase()
        // Тут можно навигацию или обновление состояния вызывать
    }
}


