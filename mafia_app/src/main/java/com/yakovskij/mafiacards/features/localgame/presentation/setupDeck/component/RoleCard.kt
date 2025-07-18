package com.yakovskij.mafiacards.features.localgame.presentation.setupDeck.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yakovskij.mafiacards.R
import com.yakovskij.mafiacards.core.ui.components.StyledCard
import com.yakovskij.mafiacards.core.ui.components.styled.StyledIconButton
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme

@Composable
fun RoleCard(
    name: String,
    count: Int,
    isLocked: Boolean,
    imageRes: Int,
    onCountChange: ((Int) -> Unit)? = null
) {
    StyledCard (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .height(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(8.dp))

            if (isLocked) {
                Text(
                    "$count",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { onCountChange?.invoke(count - 1) }, enabled = count > 0) {
                        Text("-", fontSize = 20.sp)
                    }
                    Text(
                        "$count",
                        modifier = Modifier.width(32.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    StyledIconButton(icon = Icons.Default.Add, onClick = { onCountChange?.invoke(count + 1) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoleCardDeckSetupPreview() {
    MafiaCardsTheme {
        RoleCard(
            name = "Мирный",
            count = 3,
            isLocked = false,
            imageRes = R.drawable.civmale,
            onCountChange = { },
        )
    }
}
