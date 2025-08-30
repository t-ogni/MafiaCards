package com.yakovskij.mafiacards.core.ui.themepacks

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// День
internal val DayColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3), // голубой
    secondary = Color(0xFFFFC107), // жёлтый
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF1F1F1),
    onPrimary = Color.Companion.White,
    onBackground = Color.Companion.Black
)

// Ночь
internal val NightColorScheme = darkColorScheme(
    primary = Color(0xFF0D47A1), // тёмно-синий
    secondary = Color(0xFFB39DDB), // сиреневый
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Companion.White,
    onBackground = Color.Companion.White
)

// Классическая (винная)
internal val ClassicColorScheme = darkColorScheme(
    primary = Color(0xFF800020), // бордовый
    secondary = Color(0xFFD4AF37), // золотой акцент
    background = Color(0xFF2C2C2C),
    surface = Color(0xFF3A3A3A),
    onPrimary = Color.Companion.White,
    onBackground = Color(0xFFEFEFEF)
)