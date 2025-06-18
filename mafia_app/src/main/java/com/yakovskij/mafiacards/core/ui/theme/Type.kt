package com.yakovskij.mafiacards.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.sp
import com.yakovskij.mafiacards.R

val MafiaTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.cinzel_bold)),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 72.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_slab_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_slab_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
)