package com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yakovskij.mafiacards.R
import com.yakovskij.mafiacards.core.ui.components.StyledHeadlineSmall
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme

/**
 * Высоко‑уровневый компоновщик «Газета».
 * Рисует NewspaperCanvas и накладывает:
 *  1. Заголовок (появляется сразу)
 *  2. Контент, «печатающийся» TypewriterText.
 */
@Composable
fun GazettePage(
    header: String,
    contentLines: List<String>,
    modifier: Modifier = Modifier,
    typingDelayMillis: Long = 40L,
    fontFamily: FontFamily = FontFamily(Font(R.font.typewriter)),
    textStyle: TextStyle = LocalTextStyle.current
) {
    val layoutInfo = remember { mutableStateOf<NewspaperLayoutInfo?>(null) }

    val paperTopColor = Color(0xFFF2EAD3)
    val paperBottomColor = Color(0xFFD7CBB1)
    val headerBarColor = Color(0xFF6F4C2B)
    val headerStrokeColor = Color(0xFFC08043)
    val contentTextColor = Color(0xFF1F1005)
    val headerTextColor = Color(0xFFE8E5DF)

    Box(
        modifier = modifier.defaultMinSize(40.dp, 50.dp)
    ) {
        NewspaperCanvas(
            modifier = Modifier.matchParentSize(),
            onLayoutCalculated = { layoutInfo.value = it }
        )
        layoutInfo.value?.let { info ->
            val density = LocalDensity.current

            // Пиксельные координаты
            val headerTopLeft = info.headerTopLeft
            val headerBottomRight = info.headerBottomRight

            val headerWidthPx = headerBottomRight.x - headerTopLeft.x
            val headerHeightPx = headerBottomRight.y - headerTopLeft.y

            // Перевод в dp
            val headerWidthDp = with(density) { headerWidthPx.toDp() }
            val headerHeightDp = with(density) { headerHeightPx.toDp() }


            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = headerTopLeft.x.toInt(),
                            y = headerTopLeft.y.toInt()
                        )
                    }
                    .size(width = headerWidthDp, height = headerHeightDp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = header,
                    fontFamily = fontFamily,
                    style = textStyle,
                    fontSize = 32.sp,
                    lineHeight = 24.sp,
                    color = headerTextColor
                )
            }

            // Пиксельные координаты
            val contentTopLeft = info.contentTopLeft
            val contentBottomRight = info.contentBottomRight

            val contentWidthPx = contentBottomRight.x - contentTopLeft.x
            val contentHeightPx = contentBottomRight.y - contentTopLeft.y

            // Перевод в dp
            val contentWidthDp = with(density) { contentWidthPx.toDp() }
            val contentHeightDp = with(density) { contentHeightPx.toDp() }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = contentTopLeft.x.toInt(),
                            y = contentTopLeft.y.toInt()
                        )
                    }
                    .size(width = contentWidthDp, height = contentHeightDp),
                contentAlignment = Alignment.TopStart
            ) {
                TypewriterText(
                    fullText = (contentLines.map { "- $it" }).joinToString("\n"),
                    delayMillis = typingDelayMillis,
                    color = contentTextColor,
                    fontFamily = fontFamily
                )
            }
        }
    }
}

/**
 * Мини‑демо‑превью (замени на ваш NightResultsScreen).
 */
@Preview(showBackground = true)
@Composable
fun GazettePagePreview() {
    MafiaCardsTheme {
        GazettePage(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            header = "ГАЗЕТА ГОРОДА",
            contentLines = listOf("KILLER ON THE LOOSE", "1 TOWN TEAM MEMBER KILLED..."),
            typingDelayMillis = 20L
        )
    }
}
