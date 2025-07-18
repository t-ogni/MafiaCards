package com.yakovskij.mafiacards.features.localgame.presentation.game.nightresults.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme
import kotlin.random.Random

/**
 * Структура с описанием области размещения контента внутри газеты.
 */
data class NewspaperLayoutInfo(
    val paperLeft: Float,
    val paperTop: Float,
    val paperWidth: Float,
    val paperHeight: Float,
    val headerTopLeft: Offset,
    val headerBottomRight: Offset,
    val contentTopLeft: Offset,
    val contentBottomRight: Offset,

    )

/**
 * Рисует «газету» с несколькими слоями бумаги, тенями и заголовочной плашкой.
 * Возвращает [NewspaperLayoutInfo] для точного позиционирования текста.
 *
 * @param modifier контейнер для размеров/позиционирования
 * @param shadowOffset величина смещения тени вниз‑вправо
 * @param pageDepth сколько «подложенных» листов рисовать сзади (эффект стопки)
 */
@Composable
fun NewspaperCanvas(
    modifier: Modifier = Modifier,
    shadowOffset: Dp = 6.dp,
    pageDepth: Int = 2,
    onLayoutCalculated: (NewspaperLayoutInfo) -> Unit = {},
    paperTopColor: Color = Color(0xFFD6A667),
    paperBottomColor: Color = Color(0xFFAB6936),
    headerBarColor: Color = Color(0xFF6F4C2B),
    headerStrokeColor: Color = Color(0xFFC08043)
) {
    val density = LocalDensity.current

    Canvas(modifier = modifier) {
        val paperW = size.width * 0.94f
        val paperH = size.height * 0.96f
        val left = (size.width - paperW) / 2f
        val top = (size.height - paperH) / 2f
        val corner = with(density) { 4.dp.toPx() }
        val shadow = with(density) { shadowOffset.toPx() }
        val sidePad = with(density) { 16.dp.toPx() }

        // тень
        drawRoundRect(
            color = Color.Black.copy(alpha = 0.35f),
            topLeft = Offset(left + shadow, top + shadow),
            size = Size(paperW, paperH),
            cornerRadius = CornerRadius(corner)
        )

        // стопка подложек повёрнутая на 1 градус
        rotate(-1f) {
            drawRoundRect(
                color = paperBottomColor,
                topLeft = Offset(left-10, top-5),
                size = Size(paperW+20, paperH),
                cornerRadius = CornerRadius(corner)
            )
        }

        drawRoundRect(
            brush = Brush.verticalGradient(0.8f to paperTopColor, 1f to paperBottomColor),
            topLeft = Offset(left, top),
            size = Size(paperW, paperH),
            cornerRadius = CornerRadius(corner)
        )

        val headerHeight = with(density) { 80.dp.toPx() }
        val headerStartOffset = Offset(left + sidePad, top + sidePad)
        val headerSize = Size(paperW - sidePad * 2, headerHeight - sidePad)
        val headerEndOffset = Offset(left + sidePad + headerSize.width, top + sidePad + headerSize.height)
        drawRoundRect(
            color = headerBarColor,
            topLeft = headerStartOffset,
            size = headerSize,
            cornerRadius = CornerRadius(with(density) { 2.dp.toPx() })
        )

        val horizontalOffset = with(density) { 8.dp.toPx() }
        val verticalOffset = with(density) { 8.dp.toPx() }
        val headerStrokeWidth = with(density) { 4.dp.toPx() }

        val headerTopLeft = Offset(headerStartOffset.x, headerStartOffset.y + verticalOffset + headerStrokeWidth)
        val headerBottomRight = Offset(headerEndOffset.x, headerEndOffset.y - verticalOffset - headerStrokeWidth)


        drawLine(
            color = headerStrokeColor,
            start = Offset(headerStartOffset.x + horizontalOffset, headerStartOffset.y + verticalOffset),
            end = Offset(headerEndOffset.x - horizontalOffset, headerStartOffset.y + verticalOffset),
            strokeWidth = headerStrokeWidth
        )

        drawLine(
            color = headerStrokeColor,
            start = Offset(headerStartOffset.x + horizontalOffset, headerEndOffset.y - verticalOffset),
            end = Offset(headerEndOffset.x - horizontalOffset, headerEndOffset.y - verticalOffset),
            strokeWidth = headerStrokeWidth
        )

        val lineStartY = top + paperH - with(density) { 32.dp.toPx() }
        val lineSpacing = with(density) { 8.dp.toPx() }
        repeat(3) { idx ->
            val y = lineStartY + idx * lineSpacing
            drawLine(
                color = headerBarColor,
                start = Offset(left + sidePad, y),
                end = Offset(left + paperW - sidePad, y),
                strokeWidth = with(density) { 1.dp.toPx() }
            )
        }

        val contentTopLeft = Offset(headerStartOffset.x, headerEndOffset.y + with(density) { 20.dp.toPx() })
        val contentBottomRight = Offset(headerEndOffset.x, lineStartY - with(density) { 8.dp.toPx() })

        onLayoutCalculated(
            NewspaperLayoutInfo(
                paperLeft = left,
                paperTop = top,
                paperWidth = paperW,
                paperHeight = paperH,
                headerTopLeft = headerTopLeft,
                headerBottomRight = headerBottomRight,
                contentTopLeft = contentTopLeft,
                contentBottomRight = contentBottomRight
            )
        )
//        debug dot
//        drawRect(Color.Red, headerTopLeft,  Size(20f, 20f))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewspaper() {
    MafiaCardsTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f)
        ) {
            NewspaperCanvas(modifier = Modifier.matchParentSize())

        }
    }
}


