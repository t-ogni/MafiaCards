package com.yakovskij.mafiacards.features.localgame.presentation.game.dealCards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DealCardsScreen(
    viewModel: DealCardsViewModel = hiltViewModel(),
    onGameStart: () -> Unit
) {
    val state by viewModel.uiState

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clickable(enabled = !state.allCardsDealt) { viewModel.onCardClick() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.allCardsDealt) {
                Text("Все роли розданы", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(16.dp))
                Button(onClick = {
                    onGameStart()
                }) {
                    Text("Запустить ночь")
                }
            } else {
                Text("Игрок: ${state.currentPlayer?.name}", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(24.dp))
                Card(
                    modifier = Modifier.size(200.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.isCardFlipped) Color.White else Color.DarkGray
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        if (state.isCardFlipped) {
                            Text(
                                state.currentPlayer?.role?.title ?: "Неизвестно",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text("Нажмите, чтобы открыть", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
