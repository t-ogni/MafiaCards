package com.yakovskij.mafiacards.features.localgame.presentation.setupDeck.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yakovskij.mafiacards.R
import com.yakovskij.mafiacards.core.ui.components.StyledCard
import com.yakovskij.mafiacards.core.ui.components.styled.StyledIconButton
import com.yakovskij.mafiacards.core.ui.theme.AccentColor
import com.yakovskij.mafiacards.core.ui.theme.DarkRed
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme
import com.yakovskij.mafiacards.core.ui.theme.MafiaWhite

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
                .fillMaxWidth().padding(top = 12.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                    .shadow(1.dp)
                    .border(1.dp, DarkRed)
                    .padding(3.dp)
                    .fillMaxWidth()
                    .heightIn(100.dp, 200.dp),

                contentAlignment = Alignment.Center){
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = name,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillHeight,
                    alignment = Alignment.CenterEnd
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                name,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(R.font.roboto_slab_bold)),
                color = LightTextColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isLocked) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "$count",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.roboto_slab_bold)),
                        color = LightTextColor
                    )
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = LightTextColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    StyledIconButton(onClick = { onCountChange?.invoke(count - 1) }, size = 40.dp) {
                        Text(
                            text = "-",
                            color = AccentColor,
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Text(
                        "$count",
                        modifier = Modifier.width(32.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.roboto_slab_bold)),
                        color = LightTextColor
                    )

                    StyledIconButton(onClick =  { onCountChange?.invoke(count + 1) }, size = 40.dp) {
                        Text(
                            text = "+",
                            color = AccentColor,
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoleCardDeckSetupPreview() {
    MafiaCardsTheme {
        Row() {
            RoleCard(
                name = "Мирный",
                count = 3,
                isLocked = false,
                imageRes = R.drawable.civmale,
                onCountChange = { },
            )
            RoleCard(
                name = "Мирный",
                count = 3,
                isLocked = false,
                imageRes = R.drawable.civmale,
                onCountChange = { },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoleCardDeckSetupPreview2() {
    MafiaCardsTheme {
        Box (modifier = Modifier.height(400.dp).width(200.dp)) {
            RoleCard(
                name = "Мирный",
                count = 4,
                isLocked = true,
                imageRes = R.drawable.civfemale,
                onCountChange = { },
            )
        }
    }
}
