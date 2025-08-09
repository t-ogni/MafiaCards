package com.yakovskij.mafiacards.features.localgame.presentation.game.dealCards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.yakovskij.mafia_engine.domain.*
import com.yakovskij.mafia_engine.domain.role.RoleType
import com.yakovskij.mafiacards.R

@Composable
fun FaceCard(
    currentPlayer: Player
) {
    val drawableId = when (currentPlayer.role) {
        RoleType.MAFIA -> R.drawable.mafia
        RoleType.CIVILIAN -> R.drawable.civfemale
        RoleType.DOCTOR -> R.drawable.doctor
        RoleType.DETECTIVE -> R.drawable.detective
        RoleType.MANIAC -> R.drawable.mafia
        RoleType.SLUT -> R.drawable.smokingwoman
        else -> R.drawable.civmale
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Фон с рамкой
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxHeight()
        )
    }


}
