package com.yakovskij.mafiacards.features.localgame.presentation.game.gameover

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yakovskij.mafiacards.core.ui.components.StyledButton
import com.yakovskij.mafiacards.core.ui.components.StyledCard
import com.yakovskij.mafiacards.core.ui.components.StyledOutlinedButton
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground

@Composable
fun GameOverScreen(onExit: () -> Unit, onNewGame: () -> Unit) {
    StyledVineBackground()
    Box(modifier = Modifier.systemBarsPadding().fillMaxSize()){
    StyledCard(modifier = Modifier.fillMaxSize()) {
        Row{
            StyledButton("Начать новую игру", onClick = onNewGame)
            StyledOutlinedButton("Выйти", onClick = onExit)
        }
    }
    }
}
