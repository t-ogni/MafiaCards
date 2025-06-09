package com.yakovskij.mafiacards.features.game.presentation

import androidx.lifecycle.ViewModel
import com.yakovskij.mafiacards.features.game.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repo: GameRepository
) : ViewModel() {
    val players = repo.getState()?.players ?: emptyList()
}
