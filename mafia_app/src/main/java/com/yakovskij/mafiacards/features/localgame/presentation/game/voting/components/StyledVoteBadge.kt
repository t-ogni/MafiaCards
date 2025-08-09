package com.yakovskij.mafiacards.features.localgame.presentation.game.voting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StyledVoteBadge(
    count: Int,
    isLeader: Boolean,
    modifier: Modifier = Modifier
) {
    if (count <= 0) return

    val backgroundColor = if (isLeader) Color.Red else Color.Gray

    Box(
        modifier = modifier
            .background(backgroundColor, shape = CircleShape)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = count.toString(),
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
