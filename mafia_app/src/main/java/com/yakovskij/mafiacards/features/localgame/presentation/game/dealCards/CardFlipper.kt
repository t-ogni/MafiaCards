package com.yakovskij.mafiacards.features.localgame.presentation.game.dealCards


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun CardFlipper(
    showBack: Boolean,
    front: @Composable () -> Unit,
    back: @Composable () -> Unit,
    onClick: () -> Unit = {},
    onFrontClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onEndAnimation: () -> Unit = {},
    onStartAnimation: () -> Unit = {},
    onEndAnimationFrontShowed: () -> Unit = {},
    onEndAnimationBackShowed: () -> Unit = {},
    onAnimationCardAtMiddle: () -> Unit = {}
) {
    val rotation by animateFloatAsState(
        targetValue = if (showBack) 0f else 180f, //
        animationSpec = tween(durationMillis = 500),
        label = "CardFlip"
    )

    val hasAnimationStarted = remember { mutableStateOf(false) }
    val startOperationExecuted = remember { mutableStateOf(false) }
    val middleOperationExecuted = remember { mutableStateOf(false) }
    val endOperationExecuted = remember { mutableStateOf(true) } // чтобы не сработало на первом composition

    LaunchedEffect(rotation) {
        if (!hasAnimationStarted.value &&
            (rotation > 0.1f && rotation < 179.9f)
        ) {
            hasAnimationStarted.value = true
        }

        if (!hasAnimationStarted.value) return@LaunchedEffect

        // Старт анимации
        if (!startOperationExecuted.value &&
            (
                (!showBack && rotation > 0.1f && rotation < 90f) ||
                (showBack && rotation < 179.9f && rotation > 90f)
            )
        ) {
            startOperationExecuted.value = true
            middleOperationExecuted.value = false
            onStartAnimation()
        }

        // Середина флипа
        if (!middleOperationExecuted.value &&
            ((showBack && rotation < 90f) || (!showBack && rotation > 90f))
        ) {
            middleOperationExecuted.value = true
            endOperationExecuted.value = false
            onAnimationCardAtMiddle()
        }

        // Конец анимации
        if (!endOperationExecuted.value &&
            (
                    (!showBack && rotation >= 175f) ||
                            (showBack && rotation <= 5f)
                    )
        ) {
            onEndAnimation()
            if (showBack) {
                onEndAnimationBackShowed()
            } else {
                onEndAnimationFrontShowed()
            }
            endOperationExecuted.value = true
            startOperationExecuted.value = false
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .aspectRatio(2f / 3f) // Пропорция 2:3
        .graphicsLayer {
            rotationY = rotation
            cameraDistance = 18 * density
        }) {
        if (rotation < 90f) {
            Box(modifier = Modifier.graphicsLayer { rotationY = 0f }
                .clickable(enabled = endOperationExecuted.value, onClick = {
                    onClick()
                    onBackClick()
                })) {
                back()
            }
        } else {
            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }
                .clickable(enabled = endOperationExecuted.value, onClick = {
                    onClick()
                    onFrontClick()
                })) {
                front()
            }
        }
    }



}
