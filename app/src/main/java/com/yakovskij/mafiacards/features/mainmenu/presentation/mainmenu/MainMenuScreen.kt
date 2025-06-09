package com.yakovskij.mafiacards.features.mainmenu.presentation.mainmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuScreen (onLocalGameClick: () -> Unit = {}) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(23.dp), contentAlignment = Alignment.Center) {
            Button(onClick = onLocalGameClick) { Text("Локальная игра") }
        }
    }
}