package com.yakovskij.mafiacards.features.localgame.presentation.game.discussion

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.R
import com.yakovskij.mafiacards.core.ui.theme.DarkTextColor

@Composable
fun DayDiscussionScreen(
    viewModel: DayDiscussionViewModel = hiltViewModel(),
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

    val context = LocalContext.current
    val soundManager = remember { SoundEffectManager(context) }

    DisposableEffect (Unit) {
        onDispose {
            soundManager.release()
        }
    }

    val typewriterFont = FontFamily(Font(R.font.typewriter))

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFF2EAD3), Color(0xFFD7CBB1)) // светло-желтый, как старая бумага
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ГАЗЕТА ГОРОДА",
                fontFamily = typewriterFont,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .border(BorderStroke(2.dp, Color.Black))
                    .padding(8.dp)
            )

            HorizontalDivider(thickness = 1.dp, color = Color.DarkGray)
            Spacer(Modifier.height(8.dp))
        }
        Column {

            if (uiState.nightResults.isEmpty()) {
                Text("Ночь прошла спокойно.")
            } else {
                uiState.nightResults.forEach { line ->
                    TypewriterText(fullText = "• $line")
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "⏱ ${uiState.dayTimeSecondsLeft} секунд",
                fontFamily = typewriterFont,
                style = MaterialTheme.typography.bodyLarge,
                color = DarkTextColor
            )


            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.stopTimer()
                    onNextPhase()
                },
                enabled = uiState.dayTimeSecondsLeft > 0
            ) {
                Text("Следующая фаза")
            }
        }
    }
}
