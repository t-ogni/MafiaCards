package com.yakovskij.mafiacards.features.localgame.presentation.game.dealCards

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yakovskij.mafiacards.features.game.data.GameRepository
import com.yakovskij.mafiacards.features.game.domain.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class DealCardsViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(DealCardsUiState())
    val uiState: State<DealCardsUiState> = _uiState

    private val players: List<Player> = gameRepository.getState()?.players ?: emptyList()

    init {
        _uiState.value = DealCardsUiState(
            currentPlayer = players.firstOrNull(),
            totalPlayers = players.size
        )
    }

    fun onCardClick() {
        val state = _uiState.value
        when {
            !state.isCardFlipped -> {
                // Открываем карту
                _uiState.value = state.copy(isCardFlipped = true)
            }

            state.currentIndex < players.lastIndex -> {
                // Следующий игрок
                val nextIndex = state.currentIndex + 1
                _uiState.value = state.copy(
                    currentIndex = nextIndex,
                    currentPlayer = players[nextIndex],
                    isCardFlipped = false
                )
            }

            else -> {
                // Все игроки посмотрели карты
                _uiState.value = state.copy(
                    isCardFlipped = true,
                    allCardsDealt = true
                )
            }
        }
    }
}


