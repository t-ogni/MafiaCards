package com.yakovskij.mafiacards.features.localgame.presentation.game.night.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.yakovskij.mafia_engine.domain.Player
import kotlin.collections.isNotEmpty

@Composable
fun CardFrontSide(
    promptText: String,
    availableTargets: List<Player>,
    isActionTaken: Boolean,
    onActionSelected: (Player?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = 180f // <-- поворачиваем обратно для читаемого текста
            },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = promptText, style = MaterialTheme.typography.bodyLarge)

            if (availableTargets.isNotEmpty()) {
                availableTargets.forEach { target ->
                    Button(
                        onClick = { onActionSelected(target) },
                        enabled = !isActionTaken
                    ) {
                        Text(target.name)
                    }
                }
            } else {
                Button(
                    onClick = { onActionSelected(null) },
                    enabled = !isActionTaken
                ) {
                    Text("Продолжить")
                }
            }
        }
    }
}
