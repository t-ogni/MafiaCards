package com.yakovskij.mafiacards.core.ui.components.styled

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yakovskij.mafiacards.core.ui.theme.AccentBorderColor
import com.yakovskij.mafiacards.core.ui.theme.AccentColor
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme

@Composable
fun StyledIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    buttonColor: Color = LightTextColor,
    iconColor: Color = AccentColor,
) {

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(buttonColor)
            .border(BorderStroke(4.dp, AccentBorderColor), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,

            modifier = Modifier.size(size / 1.6f)

        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStyledIconButton() {
    MafiaCardsTheme {
        Row (Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceAround){
            StyledIconButton(
                icon = Icons.Default.Add,
                contentDescription = "Добавить",
                onClick = {}
            )
            StyledIconButton(
                icon = Icons.Default.Delete,
                contentDescription = "Добавить",
                onClick = {}
            )
            StyledIconButton(
                icon = Icons.Default.Edit,
                contentDescription = "Добавить",
                onClick = {}
            )
            StyledIconButton(
                icon = Icons.Default.Menu,
                contentDescription = "Добавить",
                onClick = {}
            )
        }
    }
}
