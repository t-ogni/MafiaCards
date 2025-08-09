package com.yakovskij.mafiacards.features.localgame.presentation.game.helloday

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yakovskij.mafia_engine.domain.role.TeamSide
import com.yakovskij.mafiacards.features.localgame.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Dictionary
import javax.inject.Inject

@HiltViewModel
class HellodayViewModel @Inject constructor(
    private val gameRepository: GameRepository,
) : ViewModel() {
    private val _uiState = mutableStateOf(HellodayUiState(
        teamCounts = getTeamsideCounts()
    ))
    var uiState: State<HellodayUiState> = _uiState

    fun getTeamsideCounts(): Map<TeamSide, Int>  {
        var counts = emptyMap<TeamSide, Int>()
        gameRepository
            .getEngine()
            .getPlayers()
            .filter { it.role != null }
            .groupingBy { it.role!!.side }
            .eachCount()
        return counts
    }
}