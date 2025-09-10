package com.yakovskij.mafiacards.core.errorhandler

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlobalErrorBanner(
    onExit: () -> Unit,
    onRestart: () -> Unit
) {
    val error by ErrorManager.currentError

    if (error != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(24.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Ошибка", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(error!!.message)
                    Spacer(Modifier.height(16.dp))
                    if (error!!.isCritical) {
                        Button(onClick = onExit) {
                            Text("Выйти")
                        }
                    } else {
                        Button(onClick = onRestart) {
                            Text("Назад и перезапустить")
                        }
                    }
                }
            }
        }
    }
}
