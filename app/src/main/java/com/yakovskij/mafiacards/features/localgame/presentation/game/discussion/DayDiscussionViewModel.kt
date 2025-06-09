package com.yakovskij.mafiacards.features.localgame.presentation.game.discussion

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yakovskij.mafiacards.features.game.data.GameRepository
import com.yakovskij.mafiacards.features.game.domain.Player
import com.yakovskij.mafiacards.features.localgame.presentation.game.dealCards.DealCardsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class DayDiscussionViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(DayDiscussionUiState())
    val uiState: State<DayDiscussionUiState> = _uiState

    init {
        _uiState.value = DayDiscussionUiState()
    }

}