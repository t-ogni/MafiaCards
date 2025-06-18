package com.yakovskij.mafiacards.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MafiaColorScheme = darkColorScheme(
    primary = DarkRed,         // Темно-бордовый
    onPrimary = LightTextColor,
    background = DeepBlack,      // Почти черный фон
    onBackground = LightTextColor,
    surface = SurfaceBlue,         // Глубокий тёмно-синий
    onSurface = MafiaWhite,
    secondary = GoldAccent,       // Акцент — золотистый
    onSecondary = Color.Black
)

@Composable
fun MafiaCardsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MafiaColorScheme,
        typography = MafiaTypography,
        shapes = Shapes(),
        content = content
    )
}