package com.yakovskij.mafiacards.core.ui.themepacks

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.yakovskij.mafiacards.core.ui.themepacks.MafiaTypography

enum class MafiaThemeType {
    DAY, NIGHT, CLASSIC
}

@Composable
fun MafiaTheme(
    themeType: MafiaThemeType,
    content: @Composable () -> Unit
) {
    val colors = when (themeType) {
        MafiaThemeType.DAY -> DayColorScheme
        MafiaThemeType.NIGHT -> NightColorScheme
        MafiaThemeType.CLASSIC -> ClassicColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = MafiaTypography,
        content = content
    )
}
