package com.yakovskij.mafiacards.features.localgame.presentation.game.voting

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme


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

    if(uiState.isVotingFinished){
        VotingEndedScreen(viewModel, onNextPhase)
    } else {
        PlayerVotingScreen(viewModel)
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
        )
    }
}
