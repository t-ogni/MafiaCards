package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yakovskij.mafiacards.core.ui.theme.AccentBorderColor
import com.yakovskij.mafiacards.core.ui.theme.AccentColor
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme
import com.yakovskij.mafiacards.core.ui.theme.darken

@Composable
fun StyledButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    height: Dp = 60.dp,
    textSize: Int = 16,
    enabled: Boolean = true,
) {
    // базовые цвета
    val start = darken(AccentColor, 0.1f)          // чуть темнее для левого края
    val end = AccentColor                          // основной цвет для правого края
    val disabledBg = darken(AccentColor, 0.25f)    // сильно притушенный для disabled
    val borderColor = if (enabled) AccentBorderColor  else darken(AccentBorderColor, 0.3f)

    val backgroundBrush: Brush = if (enabled) {
        Brush.horizontalGradient(listOf(start, end))
    } else {
        Brush.verticalGradient(colors = listOf(disabledBg, disabledBg))
    }
    // сам компонент
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(height),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(3.dp, borderColor),
        elevation = ButtonDefaults.buttonElevation(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,          // убираем заливку Button‑а
            contentColor = LightTextColor,
            disabledContainerColor = disabledBg,
            disabledContentColor = LightTextColor.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues()                 // убираем внутренние отступы, чтобы Box занял всю площадь
    ) {
        // слой‑фон
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    backgroundBrush,
                    shape = RoundedCornerShape(20.dp),
                    alpha = 1f
                )
                .padding(horizontal = 24.dp),             // ручной внутренний отступ
            contentAlignment = Alignment.Center
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
}


@Preview(showBackground = true)
@Composable
fun PreviewStyledButtons() {
    MafiaCardsTheme {
        StyledVineBackground()
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StyledButton(text = "Большой", height = 72.dp, textSize = 20)
            StyledButton(text = "Средний", height = 60.dp, textSize = 16)
            StyledButton(text = "Маленький", height = 48.dp, textSize = 14)
            StyledButton(text = "Disabled", enabled = false)
        }
    }
}
