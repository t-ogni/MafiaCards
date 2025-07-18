package com.yakovskij.mafiacards.core.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

val DarkRed = Color(0xFF3a0907)


val DarkestBackgroundGradientPoint = darken(DarkRed, 0.05f)
val LightestBackgroundGradientPoint = lighten(DarkRed, 0.05f)
//val DarkestBackgroundGradientPoint = Color(0xFF320909)
//val LightestBackgroundGradientPoint = Color(0xFF4c0e0e)


val AccentColor = Color(0xFF941b0f)
val AccentBorderColor = Color(0xFFe63b2c)
val LightTextColor = Color(0xFFFCEFD9)
val DarkTextColor =  Color(0xFF2D2D2D)
val DeepDark = Color(0xFF1E1E1E)
val DeepBlack = Color(0xFF121212)
val SurfaceBlue = Color(0xFF1C1F24)
val GoldAccent = Color(0xFFD4AF37)
val MafiaWhite = Color(0xFFF0EAD6)


fun Color.toHsl(): FloatArray {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(this.toArgb(), hsl)
    return hsl
}

fun hslToColor(hsl: FloatArray): Color {
    return Color(ColorUtils.HSLToColor(hsl))
}

fun darken(color: Color, amount: Float): Color {
    val hsl = color.toHsl()
    hsl[2] = (hsl[2] - amount).coerceIn(0f, 1f) // Lightness
    return hslToColor(hsl)
}

fun lighten(color: Color, amount: Float): Color {
    val hsl = color.toHsl()
    hsl[2] = (hsl[2] + amount).coerceIn(0f, 1f) // Lightness
    return hslToColor(hsl)
}
