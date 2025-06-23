package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yakovskij.mafiacards.core.ui.theme.AccentBorderColor
import com.yakovskij.mafiacards.core.ui.theme.AccentColor
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme

@Composable
fun StyledButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    height: Dp = 60.dp,
    textSize: Int = 16,
    enabled: Boolean = true
) {

    val backgroundColor = AccentColor
    val textColor = LightTextColor

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),

        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(4.dp, AccentBorderColor),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = textSize.sp,
                letterSpacing = 2.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStyledButtons() {
    MafiaCardsTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StyledButton(text = "Большой", height = 72.dp, textSize = 20)
            StyledButton(text = "Средний", height = 60.dp, textSize = 16)
            StyledButton(text = "Маленький", height = 48.dp, textSize = 14)
            StyledButton(text = "Disabled", enabled = false)
        }
    }
}
