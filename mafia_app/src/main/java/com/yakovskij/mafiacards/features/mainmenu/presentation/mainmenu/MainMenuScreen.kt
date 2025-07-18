package com.yakovskij.mafiacards.features.mainmenu.presentation.mainmenu


import com.yakovskij.mafiacards.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.yakovskij.mafiacards.core.ui.components.StyledButton
import com.yakovskij.mafiacards.core.ui.components.StyledHeadlineLarge
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor

@Composable
fun MainMenuScreen(
    onCreateGame: () -> Unit = {},
    onJoinGame: () -> Unit = {},
    onHowToPlay: () -> Unit = {},
    onStats: () -> Unit = {}) {

    StyledVineBackground()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            StyledHeadlineLarge(
                text = "MAFIA",
                modifier = Modifier.padding(top = 12.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                StyledButton("Создать комнату", textSize = 22, onClick = onCreateGame, modifier = Modifier.fillMaxWidth())
                StyledButton("Присоединиться", textSize = 18, onClick = onJoinGame, modifier = Modifier.fillMaxWidth(0.9f))
                StyledButton("Как играть?", textSize = 16, onClick = onHowToPlay, modifier = Modifier.fillMaxWidth(0.7f))
            }


            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.smokingwoman),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxHeight()
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
               StyledButton("Моя статистика", onClick = onStats, modifier = Modifier.fillMaxWidth(0.9f))
            }
        }
    }
}