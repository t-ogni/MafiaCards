package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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


@Composable
fun VotingEndedScreen(
    viewModel: VotingViewModel = hiltViewModel(),
    onConfirmVotingEnded: () -> Unit
) {
    StyledVineBackground()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        StyledOutlinedCard {
            Text("Голосование завершено...")
            StyledOutlinedButton(text = "Перейти к результатам ->", onClick =
                {
                    viewModel.calculateVotes()
                    onConfirmVotingEnded()
                }
            )
        }
    }
}

@Composable
fun VoterVoteRow(
    voter: Player,
    selected: Player?,
    allCandidates: List<Player>,
    onVoteChange: (Player) -> Unit
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
