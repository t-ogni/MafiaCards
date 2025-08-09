package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafia_engine.domain.Player
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedButton
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedCard
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground
import com.yakovskij.mafiacards.features.localgame.presentation.game.voting.components.StyledVoteBadge


@Composable
fun VotingEndedScreen(
    viewModel: VotingViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState
    val votes = uiState.votes
    val candidates = uiState.votingCandidates

    val voteCountMap = uiState.votes.values.groupingBy { it.id }.eachCount() // Map<playerId, count>
    val maxVotes = voteCountMap.maxOfOrNull { it.value } ?: 0
    val leaders = voteCountMap.filterValues { it == maxVotes }.keys // Set<Int>

    StyledVineBackground()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(vertical = 16.dp, horizontal = 8.dp),
    ) {
        Text(
            text = "Голоса игроков",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(uiState.votes.keys.toList()) { voter ->
                val votedFor = votes[voter]

                VoterVoteRow(
                    voter = voter,
                    selected = votedFor,
                    allCandidates = candidates,
                    onVoteChange = { newTarget ->
                        viewModel.updateVote(voter.id, newTarget.id)
                    },
                    voteCount = voteCountMap[voter.id] ?: 0,
                    isLeader = voter.id in leaders
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        StyledOutlinedButton(
            text = "Перейти к результатам →",
            onClick = {
                viewModel.calculateVotes()
                onNextPhase()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}


@Composable
fun VoterVoteRow(
    voter: Player,
    selected: Player?,
    allCandidates: List<Player>,
    onVoteChange: (Player) -> Unit,
    voteCount: Int,
    isLeader: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = voter.name,
            modifier = Modifier.weight(1f)
        )
        Box {
            if (voteCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 10.dp, y = (-6).dp)
                ) {
                    StyledVoteBadge(
                        count = voteCount,
                        isLeader = isLeader,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 10.dp, y = (-6).dp)
                    )
                }
            }
        }


        if (!voter.canVote) {
            Icon(
                imageVector = Icons.Default.Close, // или Close
                contentDescription = "Не голосует",
                tint = MaterialTheme.colorScheme.error
            )
        } else {
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(selected?.name ?: "Не выбрано")
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Изменить"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    allCandidates.forEach { candidate ->
                        DropdownMenuItem(
                            text = { Text(candidate.name) },
                            onClick = {
                                expanded = false
                                onVoteChange(candidate)
                            }
                        )
                    }
                }
            }
        }
    }
}
