package com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.R
import com.yakovskij.mafiacards.core.ui.components.StyledButton
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme
import com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults.components.GazettePage


@Composable
fun NightResultsScreen(
    viewModel: NightResultsViewModel = hiltViewModel(),
    onNextPhase: () -> Unit
) {
    val uiState by viewModel.uiState

    LaunchedEffect(uiState.shouldInit) {
        if (uiState.shouldInit) {
            viewModel.startDiscussion()
        }
    }

    LaunchedEffect(uiState.isViewingEnded) {
        if (uiState.isViewingEnded) {
            viewModel.resetDiscussionState()
            onNextPhase()
        }
    }

    val typewriterFont = FontFamily(Font(R.font.typewriter))
    StyledVineBackground()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(8.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GazettePage(
                modifier = Modifier.fillMaxWidth().weight(1f),
                header = "ГАЗЕТА ГОРОДА",
                contentLines = uiState.nightResults,
                typingDelayMillis = 20L
            )

            Spacer(Modifier.height(8.dp))
            Box(contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = "⏱ ${uiState.dayTimeSecondsLeft} секунд",
                    fontFamily = typewriterFont,
                    style = MaterialTheme.typography.bodyLarge,
                    color = LightTextColor
                )
            }


            Spacer(Modifier.height(8.dp))

            StyledButton (
                text = "Следующая фаза",
                onClick = {
                    viewModel.stopTimer()
                    onNextPhase()
                },
                enabled = uiState.dayTimeSecondsLeft > 0
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNewspaper() {
    val viewModel = remember { FakeNightResultsViewModel() }
    MafiaCardsTheme {
        NightResultsScreen(viewModel = viewModel, onNextPhase = {})
    }
}