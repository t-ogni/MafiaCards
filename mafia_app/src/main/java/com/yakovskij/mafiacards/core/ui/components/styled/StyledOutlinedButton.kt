package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yakovskij.mafiacards.core.ui.theme.AccentBorderColor
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme
import com.yakovskij.mafiacards.core.ui.theme.MafiaWhite
import com.yakovskij.mafiacards.core.ui.theme.darken
import com.yakovskij.mafiacards.core.ui.theme.lighten

@Composable
fun StyledOutlinedButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    height: Dp = 60.dp,
    textSize: Int = 16,
    enabled: Boolean = true,
) {
    val borderColor = if (enabled) AccentBorderColor
    else lighten(AccentBorderColor, 0.4f)
    val textColor   = if (enabled) AccentBorderColor
    else lighten(AccentBorderColor, 0.4f)
    val disabledContainer = darken(MafiaWhite, 0.1f)  // лёгкая «дымка» для отключённого
    val disabledContent   = textColor.copy(alpha = 0.7f)

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(height),
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(3.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor         = Color.Transparent,
            contentColor           = textColor,
            disabledContainerColor = disabledContainer,
            disabledContentColor   = disabledContent
        ),
        elevation = ButtonDefaults.buttonElevation(8.dp)   // небольшая тень, как у filled‑версии
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = textSize.sp,
                letterSpacing = 2.sp
            )
        )
    }
}

//
// Быстрый превью‑тест

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedButtons() {
    MafiaCardsTheme {
        StyledVineBackground()
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StyledOutlinedButton("Primary")
            StyledOutlinedButton("Small", height = 48.dp, textSize = 14)
            StyledOutlinedButton("Disabled", enabled = false)
        }
    }
}