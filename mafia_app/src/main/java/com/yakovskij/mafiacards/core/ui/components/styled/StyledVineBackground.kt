package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import com.yakovskij.mafiacards.R
import com.yakovskij.mafiacards.core.ui.theme.DarkestBackgroundGradientPoint
import com.yakovskij.mafiacards.core.ui.theme.LightestBackgroundGradientPoint
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme

@Composable
fun StyledVineBackground(modifier: Modifier = Modifier, alphaChannel: Float = 0.06f) {
    val noiseBitmap: ImageBitmap = ImageBitmap.imageResource(R.drawable.noize)
    Canvas(modifier = modifier.fillMaxSize()) {
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            alpha = (255 * alphaChannel).toInt()
            shader = android.graphics.BitmapShader(
                noiseBitmap.asAndroidBitmap(),
                android.graphics.Shader.TileMode.REPEAT,
                android.graphics.Shader.TileMode.REPEAT
            )
        }

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(LightestBackgroundGradientPoint.value),
                    Color(DarkestBackgroundGradientPoint.value),
                ),
                center = center,
                radius = size.minDimension
            ),
            size = size
        )
        drawContext.canvas.nativeCanvas.drawRect(
            0f, 0f, size.width, size.height, paint
        )
    }
}


@Preview(showBackground = true, heightDp = 700)
@Composable
fun PreviewStyledVineBackground() {
    MafiaCardsTheme {
        StyledVineBackground()
    }
}