package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import com.yakovskij.mafiacards.R

@Composable
fun VineNoizeBackground(modifier: Modifier = Modifier, alphaChannel: Float = 0.03f) {
    val noiseBitmap: ImageBitmap = ImageBitmap.imageResource(R.drawable.noize)
    Canvas(modifier = modifier.fillMaxSize().background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF3B1F1B), // верхний темный бордовый
                Color(0xFF5A2A24), // нижний более теплый
            )
        )
    )) {
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            alpha = (255 * alphaChannel).toInt()
            shader = android.graphics.BitmapShader(
                noiseBitmap.asAndroidBitmap(),
                android.graphics.Shader.TileMode.REPEAT,
                android.graphics.Shader.TileMode.REPEAT
            )
        }

        drawContext.canvas.nativeCanvas.drawRect(
            0f, 0f, size.width, size.height, paint
        )

        // 2. Виньетка — радиальный градиент от прозрачного центра к тёмным краям
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent, Color(0xCC000000)), // можно усилить/ослабить alpha
                center = center,
                radius = size.minDimension * 0.8f
            ),
            size = size
        )
    }
}
