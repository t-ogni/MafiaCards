package com.yakovskij.mafiacards.features.localgame.presentation.game.voteresults

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafia_engine.domain.VoteResult
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedButton
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedCard
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor


@Composable
fun VoteResultsScreen(
    viewModel: VoteResultsViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState
    // Завершение ночи, возвращаем экран к первоначальному виду, к готовности прочитать gameState
    LaunchedEffect(uiState.isScreenFinished) {
        if (uiState.isScreenFinished) {
            viewModel.resetState()
            onNextPhase()
        }
    }

    LaunchedEffect(uiState.shouldInit) {
        if (uiState.shouldInit) {
            viewModel.initViewModel()
        }
    }

    StyledVineBackground()
    StyledOutlinedCard(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp)
        .aspectRatio(1 / 2f)){
        Text(uiState.voteResults)
        Spacer(Modifier.weight(1f))
        Box(contentAlignment = Alignment.CenterEnd) {
            Text(
                text = "⏱ ${uiState.showTimeSecondsLeft} секунд",
                style = MaterialTheme.typography.bodyLarge,
                color = LightTextColor
            )
        }
        StyledOutlinedButton("Продолжить", onClick = onNextPhase)
    }
}