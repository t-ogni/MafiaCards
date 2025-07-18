package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yakovskij.mafiacards.core.ui.theme.AccentColor
import com.yakovskij.mafiacards.core.ui.theme.DarkRed
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme

@Composable
fun StyledOutlinedCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, AccentColor),
        colors = CardDefaults.cardColors(containerColor = DarkRed),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column (modifier = Modifier.padding(8.dp), content = content)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStyledOutlinedCard() {
    MafiaCardsTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StyledOutlinedCard {
                StyledButton(text = "Средний", height = 60.dp, textSize = 16)
                StyledButton(text = "Маленький", height = 48.dp, textSize = 14)
                StyledButton(text = "Disabled", enabled = false)
            }
        }
    }
}