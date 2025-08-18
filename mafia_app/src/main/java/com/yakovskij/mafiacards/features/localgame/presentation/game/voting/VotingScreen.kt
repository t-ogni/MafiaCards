package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


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

    LaunchedEffect(uiState.isViewingEnded) {
        if (uiState.isViewingEnded) {
            viewModel.clearState()
            onNextPhase()
        }
    }

    var isDimmed by remember { mutableStateOf(false) }

    LaunchedEffect(isDimmed) {
        if (isDimmed) {
            delay(300)
            isDimmed = false
        }
    }

    // Альфа анимируется от 0f (нет затемнения) до 0.5f (полупрозрачный фон)
    val dimAlpha by animateFloatAsState(
        targetValue = if (isDimmed) 0.5f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "dimAlpha"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        if(uiState.isVotingFinished){
            VotingEndedScreen(viewModel, onNextPhase)
        } else {
            PlayerVotingScreen(viewModel, onVoted = {
                isDimmed = true
            })
        }

        // Затемняющий слой
        if (dimAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = dimAlpha))
            )
        }
    }
}


@Preview(
    name = "Small phone",
    device = "spec:width=780px,height=2000px,dpi=340",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Medium Phone",
    device = "spec:width=1080px,height=2340px,dpi=440",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Tablet",
    device = "spec:width=1280px,height=800px,dpi=240",
    showBackground = true,
    fontScale = 1.2f
)

@Composable
fun VotingScreenPreview_MultiDevice() {
    val fakeViewModel = remember { FakeVotingViewModel() }

    MafiaCardsTheme {
        PlayerVotingScreen(
            viewModel = fakeViewModel
        ) {
        }
    }
}
