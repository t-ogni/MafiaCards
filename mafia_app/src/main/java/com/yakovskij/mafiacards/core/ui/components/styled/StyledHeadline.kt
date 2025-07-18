package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme
import com.yakovskij.mafiacards.R


@Composable
fun StyledHeadlineLarge(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.displayLarge,
        color = LightTextColor,
        modifier = modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun StyledHeadlineMedium(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.displayMedium,
        color = LightTextColor,
        modifier = modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun StyledHeadlineSmall(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.displaySmall,
        color = LightTextColor,
        modifier = modifier.padding(vertical = 6.dp)
    )
}

@Composable
fun StyledHeadlineResponsive(
    text: String,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val width = this.maxWidth

        val fontSize = when {
            width < 200.dp -> 24.sp
            width < 300.dp -> 32.sp
            width < 400.dp -> 40.sp
            width < 500.dp -> 48.sp
            else -> 56.sp
        }

        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.cinzel_bold)),
            color = LightTextColor,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewStyledHeadline() {
    MafiaCardsTheme {
        StyledVineBackground()
        Column(verticalArrangement = Arrangement.SpaceAround) {
            StyledHeadlineLarge("MAFIA МАКС")
            StyledHeadlineMedium("МЕДИУМ VARIANT")
            StyledHeadlineSmall("МИНИ VERSION")
            Box(modifier = Modifier.fillMaxWidth()) {
                StyledHeadlineResponsive("Респонсибл")
            }
            Box(modifier = Modifier.width(500.dp)) {
                StyledHeadlineResponsive("Респонсибл")
            }
        }
    }
}