package com.yakovskij.mafiacards.features.loader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.yakovskij.mafiacards.core.ui.components.StyledVineBackground
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun LoadingScreen(onLoaded: () -> Unit = {}) {
    var isLoaded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }

    LaunchedEffect(Unit) {
        val listOfSvgPaths = listOf(
            "file:///android_asset/illustrations/smokingWoman.svg",
            "file:///android_asset/card/docCard.svg",
            "file:///android_asset/card/civCard.svg",
            "file:///android_asset/card/maniacCard.svg",
            "file:///android_asset/card/mafiaCard.svg",
        )

        withContext(Dispatchers.IO) {
            listOfSvgPaths.map { path ->
                async {
                    val request = ImageRequest.Builder(context)
                        .data(path)
                        .build()
                    imageLoader.execute(request)
                }
            }.awaitAll()

        }
        delay(50)
        isLoaded = true
        onLoaded()
    }

    if (!isLoaded) {
        StyledVineBackground()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "MAFIA",
                style = MaterialTheme.typography.displayLarge,
                color = LightTextColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            LinearProgressIndicator()
        }
    }
}
