package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yakovskij.mafiacards.core.ui.theme.AccentBorderColor
import com.yakovskij.mafiacards.core.ui.theme.AccentColor

@Composable
fun StyledCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, AccentBorderColor),
        colors = CardDefaults.cardColors(containerColor = AccentColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column (modifier = Modifier.padding(16.dp), content = content)
    }
}
