package com.yakovskij.mafiacards.features.localgame.presentation.game.night.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.yakovskij.mafia_engine.domain.Player

@Composable
fun PlayerCard(
    player: Player,
    promptText: String,
    availableTargets: List<Player>,
    isActionTaken: Boolean,
    isFrontShown: Boolean,
    onActionSelected: (Player?) -> Unit,
    onBackClicked: () -> Unit = {},
    onFrontClicked: () -> Unit = {},
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFrontShown) 180f else 0f,
        label = "CardFlip"
    )


    val offsetX by animateFloatAsState(
        targetValue = if (isActionTaken) 1000f else 0f,
        label = "CardSlide"
    )

    val cardModifier = Modifier
        .graphicsLayer {
            rotationY = rotation
            cameraDistance = 12 * density
            translationX = offsetX
        }
        .clickable { if(isFrontShown) onFrontClicked() else onBackClicked() }
        .fillMaxWidth(0.95f)
        .aspectRatio(2f / 3f)

    Box(modifier = cardModifier) {
        if (rotation <= 90f) {
            // Back side — рубашка
            CardBackSide(playerName = player.name)
        } else {
            // Front side — содержимое
            CardFrontSide(
                promptText = promptText,
                availableTargets = availableTargets,
                isActionTaken = isActionTaken,
                onActionSelected = onActionSelected
            )
        }
    }
}
