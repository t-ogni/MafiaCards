package com.yakovskij.mafiacards.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlinx.coroutines.launch

@Composable
fun StyledLazyColumn(
    modifier: Modifier = Modifier,
    itemHeightDp: Dp = 60.dp,
    spacing: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scrollbarWidth: Dp = 30.dp,
    scrollbarPadding: Dp = 4.dp,
    content: LazyListScope.() -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
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
        Row(modifier = Modifier.fillMaxSize()) {
            // Колонка со стрелками и списком
            Column(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    state = listState,
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(spacing),
                    modifier = Modifier.weight(1f)
                ) {
                    content()
                }
            }

            // Скроллбар
            if (totalItemsCount > listState.layoutInfo.visibleItemsInfo.size) {
                var scrollbarHeightPx by remember { mutableStateOf(1f) }
                val scrollOffsetPx by remember {
                    derivedStateOf {
                        val offset = listState.firstVisibleItemIndex * itemHeightPx +
                                listState.firstVisibleItemScrollOffset
                        offset
                    }
                }

                val totalContentHeightPx = itemHeightPx * totalItemsCount
                val scrollFraction = (scrollOffsetPx / (totalContentHeightPx - containerHeightPx)).coerceIn(0f, 1f)
                val thumbHeightPx = (containerHeightPx.toFloat() / totalContentHeightPx) * containerHeightPx
                val thumbOffsetY = (containerHeightPx - thumbHeightPx) * scrollFraction

                Box(
                    modifier = Modifier
                        .padding(horizontal = scrollbarPadding)
                        .width(scrollbarWidth)
                        .fillMaxHeight()
                        .pointerInput(totalItemsCount) {
                            detectTapGestures { offset ->
                                val clickedY = offset.y
                                val targetIndex = ((clickedY / containerHeightPx) * totalItemsCount).toInt()
                                    .coerceIn(0, totalItemsCount - 1)
                                coroutineScope.launch {
                                    listState.scrollToItem(targetIndex)
                                }
                            }
                        }
                ) {
                    // фон трека
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentColor)
                            .border(4.dp, AccentBorderColor)
                    )

                    // бегунок
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(with(density) { thumbHeightPx.toDp() })
                            .offset { IntOffset(0, thumbOffsetY.toInt()) }
                            .clip(RoundedCornerShape(4.dp))
                            .background(AccentBorderColor)
                    )
                }
            }
        }
    }
}
//
//@Composable
//private fun ScrollArrowButton(direction: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(32.dp)
//            .clickable { onClick() }
//            .background(Color.Gray.copy(alpha = 0.2f)),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text = if (direction == "up") "▲" else "▼", color = LightTextColor)
//    }
//}


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
