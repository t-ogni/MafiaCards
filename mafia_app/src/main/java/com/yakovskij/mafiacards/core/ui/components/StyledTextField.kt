package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yakovskij.mafiacards.core.ui.theme.AccentBorderColor
import com.yakovskij.mafiacards.core.ui.theme.AccentColor
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = LightTextColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        label = {
            Text(
                text = label,
                color = LightTextColor.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelMedium
            )
        },
        shape = RoundedCornerShape(16.dp),
//        colors = TextFieldDefaults.colors(
//            focusedBorderColor = AccentBorderColor,
//            unfocusedBorderColor = AccentBorderColor.copy(alpha = 0.5f),
//            containerColor = AccentColor,
//            textColor = LightTextColor,
//            cursorColor = LightTextColor
//        ),
        enabled = enabled
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewStyledTextField() {
    var text = "Текст"
    StyledTextField(
        value = text,
        onValueChange = {},
        label = "Имя"
    )
}