package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.core.ui.components.StyledButton
import com.yakovskij.mafiacards.core.ui.components.StyledCard
import com.yakovskij.mafiacards.core.ui.components.StyledLazyColumn
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedButton
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedCard
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor

@Composable
fun PlayerVotingScreen(
    viewModel: VotingViewModel = hiltViewModel(),
    onVoted: () -> Unit
) {
    val uiState by viewModel.uiState

    StyledVineBackground()

    Box(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(horizontal = 16.dp)) {

        Column(modifier = Modifier
            .fillMaxSize()
        ) {

            Text(
                text = "Голосование",
                style = MaterialTheme.typography.displaySmall,
                color = LightTextColor,
                modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
            )

            StyledCard {
                Text(
                    text = uiState.currentPlayer.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = LightTextColor,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            StyledLazyColumn(spacing = 4.dp, modifier = Modifier.weight(1f)) {
                items(uiState.votingCandidates) { candidat ->
                    val isSelected = uiState.selectedToVotePlayer == candidat
                    if(isSelected) {
                        StyledOutlinedCard (
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = candidat.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                color = LightTextColor
                            )
                        }
                    } else {
                        StyledCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectPlayerToVote(candidat) },
                        ) {
                            Text(
                                text = candidat.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                color = LightTextColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                StyledButton(
                    text = "Подтвердить голос",
                    onClick = { viewModel.confirmVote() },
                    enabled = uiState.selectedToVotePlayer != null
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row (modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    StyledOutlinedButton(
                        text = "Пропустить ->",
                        onClick = { viewModel.skipVote() }
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
