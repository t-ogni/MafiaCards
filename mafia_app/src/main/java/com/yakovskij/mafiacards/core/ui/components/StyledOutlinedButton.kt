package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yakovskij.mafiacards.core.ui.theme.AccentBorderColor
import com.yakovskij.mafiacards.core.ui.theme.AccentColor
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme

@Composable
fun StyledOutlinedButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, AccentBorderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = LightTextColor,
            disabledContentColor = LightTextColor.copy(alpha = 0.3f)
        ),
        enabled = enabled
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = AccentBorderColor,
                letterSpacing = 2.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStyledOutlinedButton() {
    MafiaCardsTheme {
        StyledOutlinedButton(
            text = "Текст"
        )
    }
}