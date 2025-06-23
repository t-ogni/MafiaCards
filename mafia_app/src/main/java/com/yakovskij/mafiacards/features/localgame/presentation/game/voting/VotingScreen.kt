package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.core.ui.components.StyledButton
import com.yakovskij.mafiacards.core.ui.components.StyledCard
import com.yakovskij.mafiacards.core.ui.components.StyledLazyColumn
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults.NightResultsViewModel

@Composable
fun VotingScreen(
    viewModel: VotingViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState

    LaunchedEffect(uiState.shouldInit) {
        if (uiState.shouldInit) {
            viewModel.initState()
        }
    }

    StyledVineBackground()

    Column (modifier = Modifier.systemBarsPadding()) {
        Text(
            text = "Голосование",
            style = MaterialTheme.typography.displayLarge,
            color = LightTextColor,
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
        )
        
        StyledLazyColumn(spacing = 4.dp) {
            items(uiState.votingCandidates) { candidat ->
                StyledCard(
                    modifier = Modifier.clickable(
                        onClick = { viewModel.addVote(uiState.currentPlayer, candidat) }
                    )) {
                    Text(
                        text = candidat.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = LightTextColor
                    )
                }
            }
        }
        if(uiState.selectedToVotePlayer != null)
            StyledButton("Подтвердить голос", onClick = onNextPhase)
        else
            StyledButton("Пропустить", onClick = onNextPhase)

    }
}