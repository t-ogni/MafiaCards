package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.yakovskij.mafiacards.core.ui.theme.AccentBorderColor
import com.yakovskij.mafiacards.core.ui.theme.AccentColor
import com.yakovskij.mafiacards.core.ui.theme.LightTextColor
import com.yakovskij.mafiacards.core.ui.theme.MafiaCardsTheme

@Composable
fun StyledLazyColumn(
    modifier: Modifier = Modifier,
    itemHeightDp: Dp = 60.dp, // ⚠️ Только для расчёта высоты thumb — можно убрать, если хочешь auto
    spacing: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scrollbarWidth: Dp = 30.dp,
    scrollbarPadding: Dp = 4.dp,
    content: LazyListScope.() -> Unit
) {
    val listState = remember { LazyListState() }
    val totalItemsCount by remember {
        derivedStateOf { listState.layoutInfo.totalItemsCount }
    }
    var containerHeightPx by remember { mutableStateOf(1) }

    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeightDp.toPx() }



    Box(
        modifier = modifier
            .onGloballyPositioned { layout ->
                containerHeightPx = layout.size.height
            }
    ) {
        Row {
            val needScrollbar = remember(totalItemsCount, listState.layoutInfo.visibleItemsInfo.size) {
                totalItemsCount > listState.layoutInfo.visibleItemsInfo.size
            }

            LazyColumn(
                state = listState,
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier
                    .weight(1f)
            ) {
                with(this) {
                    content()
                }
            }


            if (needScrollbar) {
                Box(
                    modifier = Modifier.padding(horizontal = scrollbarPadding)
                        .width(scrollbarWidth)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    val verticalPaddingPx = with(density) { 8.dp.toPx() * 2 }
                    val topPaddingPx = with(density) { 8.dp.toPx() }

                    val totalContentHeightPx = itemHeightPx * totalItemsCount
                    val thumbHeightPx = (containerHeightPx.toFloat() / totalContentHeightPx) * containerHeightPx
                    val scrollOffset = (listState.firstVisibleItemScrollOffset +
                            listState.firstVisibleItemIndex * itemHeightPx).toFloat()

                    val maxScrollPx = (totalContentHeightPx - containerHeightPx).coerceAtLeast(1f)
                    val scrollFraction = scrollOffset / maxScrollPx

                    val maxThumbTravelPx = (containerHeightPx - thumbHeightPx - verticalPaddingPx).coerceAtLeast(1f)
                    val thumbOffsetY = scrollFraction * maxThumbTravelPx + topPaddingPx

                    // Бэкграунд слайдера
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentColor)
                            .border(width = 4.dp, color = AccentBorderColor)
                    )

                    // Бегунок
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .offset { IntOffset(0, thumbOffsetY.toInt()) }
                            .clip(RoundedCornerShape(4.dp))
                            .background(AccentBorderColor)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, heightDp = 700)
@Composable
fun PreviewLazyColumnWithScrollbar() {
    MafiaCardsTheme {
        StyledLazyColumn(spacing = 4.dp) {
            items(100) { i ->
                StyledCard {
                    Text(
                        text = "Игрок $i",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = LightTextColor
                    )
                }
            }
        }
    }
}
