package com.yakovskij.mafiacards.features.localgame.presentation.game.prop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yakovskij.mafiacards.features.localgame.presentation.game.GameViewModel
import com.yakovskij.mafiacards.features.localgame.presentation.game.night.NightActionsViewModel

@Composable
fun PropScreen(
    viewModel: GameViewModel = hiltViewModel(),
    onAction: () -> Unit
) {
    val uiState by viewModel.uiState

    Column (modifier = Modifier.systemBarsPadding().padding(12.dp)){
        Text("Текущая фаза: ${uiState.phase.name}")
        Button(onClick = onAction) { Text("Следуюшая фаза") }
    }
}