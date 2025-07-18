package com.yakovskij.mafiacards.core.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import com.yakovskij.mafiacards.R
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun StyledAutoResizableHeadline(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LightTextColor,
    fontWeight: FontWeight = FontWeight.Normal,
    fontFamily: FontFamily = FontFamily(Font(R.font.cinzel_bold)),
    maxFontSize: TextUnit = 72.sp,
    minFontSize: TextUnit = 12.sp
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier) {
        val maxWidthPx = with(density) { maxWidth.toPx() }

        var fittedFontSize by remember(text) { mutableStateOf(maxFontSize) }

        LaunchedEffect(text, maxWidthPx) {
            // Бинарный поиск подходящего fontSize
            var low = minFontSize.value
            var high = maxFontSize.value
            var bestSize = low

            while (low <= high) {
                val mid = (low + high) / 2
                val textLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(text),
                    style = TextStyle(
                        fontSize = mid.sp,
                        fontFamily = fontFamily,
                        fontWeight = fontWeight
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )

                if (textLayoutResult.size.width <= maxWidthPx) {
                    bestSize = mid
                    low = mid + 0.5f
                } else {
                    high = mid - 0.5f
                }
            }

            fittedFontSize = bestSize.sp
        }

        Text(
            text = text,
            fontSize = fittedFontSize,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            color = color
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewStyledAutoResizableHeadline() {
    MafiaCardsTheme {
        StyledVineBackground()
        Column(verticalArrangement = Arrangement.SpaceAround) {
            Box(modifier = Modifier.fillMaxWidth()) {
                StyledAutoResizableHeadline("Респонсибл")
            }
            Box(modifier = Modifier.width(20.dp)) {
                StyledAutoResizableHeadline("Респонсибл")
            }

            Box(modifier = Modifier.width(400.dp).height(40.dp)) {
                StyledAutoResizableHeadline("Респонсибл")
            }
        }
    }
}