package com.yakovskij.mafiacards.features.localgame.presentation.game.voteresults

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedCard
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground


@Composable
fun VoteResultsScreen(
    viewModel: VoteResultsViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState

    LaunchedEffect(uiState.shouldInit) {
        if (uiState.shouldInit) {
            viewModel.startDiscussion()
        }
    }

    StyledVineBackground()
    StyledOutlinedCard(modifier = Modifier.fillMaxSize().padding(32.dp).aspectRatio(1/2f)){

    }
}