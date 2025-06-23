package com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.yakovskij.mafiacards.core.ui.theme.DarkTextColor
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    fullText: String,
    soundEffectManager: SoundEffectManager? = null,
    modifier: Modifier = Modifier,
    delayMillis: Long = 40L,
    fontFamily: FontFamily = FontFamily.Monospace,
    color: Color = DarkTextColor,
    textStyle: TextStyle = LocalTextStyle.current
) {
    var visibleText by remember { mutableStateOf("") }


    LaunchedEffect(fullText) {
        visibleText = ""
        fullText.forEachIndexed { index, _ ->
            visibleText = fullText.take(index + 1)
            soundEffectManager?.playKeySound()
            delay(delayMillis)
        }
    }

    Text(
        text = visibleText,
        style = textStyle,
        fontFamily = fontFamily,
        modifier = modifier,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        color = color
    )
}