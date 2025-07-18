package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedButton
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedCard
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground


@Composable
fun VotingEndedScreen(
    viewModel: VotingViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    StyledVineBackground()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        StyledOutlinedCard {
            Text("Голосование завершено...")
            StyledOutlinedButton(text = "Перейти к результатам ->", onClick = onNextPhase)
        }
    }

}