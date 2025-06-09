package com.yakovskij.mafiacards.features.localgame.presentation.game.discussion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DayDiscussionScreen(viewModel: DayDiscussionViewModel = hiltViewModel(), onNextPhase: () -> Unit) {

    val uiState by viewModel.uiState

    Scaffold { innerPadding ->
        Column (modifier = Modifier.padding(innerPadding)){
            Text("Результаты ночи:")
            Button(onClick = onNextPhase) { Text("Следуюшая фаза") }
        }
    }
}