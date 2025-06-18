package com.yakovskij.mafiacards.features.localgame.presentation.dealcards

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import com.yakovskij.mafia_engine.domain.RoleType
import com.yakovskij.mafiacards.features.localgame.presentation.game.dealCards.FaceCard
import com.yakovskij.mafiacards.features.localgame.presentation.game.dealCards.CardFlipper
import com.yakovskij.mafiacards.features.localgame.presentation.game.dealCards.BackCard

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DealCardsScreen(
    viewModel: DealCardsViewModel = hiltViewModel(),
    onNightStart: () -> Unit
) {
    val state by viewModel.uiState


    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clickable(enabled = !state.allCardsDealt) {
                    viewModel.flipCard()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = state.allCardsDealt,
                transitionSpec = {
                    fadeIn(tween(300)) with fadeOut(tween(300))
                },
                label = "DealOrNight"
            ) { allCardsDealt ->
                if (allCardsDealt) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Все роли розданы", style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {
                            viewModel.proceedToNight()
                            onNightStart()
                        }) {
                            Text("Начать ночь")
                        }
                    }
                } else {

                    AnimatedContent(
                        targetState = state.currentPlayer.name,
                        transitionSpec = {
                            slideInHorizontally { fullWidth -> fullWidth } + fadeIn() with
                                    slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                        },
                        label = "PlayerNameTransition"
                    ) { name ->
                        Text(
                            text = "$name, ваша карта",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }


                    Spacer(Modifier.height(16.dp))
                    Box(
                        Modifier.fillMaxWidth().height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(width = 100.dp, height = 150.dp)
                                .graphicsLayer { rotationZ = 5f }
                                .background(
                                    Color.DarkGray.copy(alpha = 0.2f),
                                    RoundedCornerShape(8.dp)
                                )
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(width = 100.dp, height = 150.dp)
                                .graphicsLayer { rotationZ = -4f }
                                .background(
                                    Color.DarkGray.copy(alpha = 0.2f),
                                    RoundedCornerShape(8.dp)
                                )
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(width = 100.dp, height = 150.dp)
                                .graphicsLayer { rotationZ = 3f }
                                .background(
                                    Color.DarkGray.copy(alpha = 0.2f),
                                    RoundedCornerShape(8.dp)
                                )
                        )

                        // Анимируем появление карточки
                        AnimatedContent(
                            targetState = state.currentPlayer,
                            transitionSpec = {
                                slideInHorizontally { it / 2 } + fadeIn() with
                                        slideOutHorizontally { -it / 2 } + fadeOut()
                            },
                            label = "CardDealAnimation"
                        ) {
                            CardFlipper(
                                showBack = !state.isCardFlipped,
                                onClick = { viewModel.flipCard() },
                                onEndAnimationBackShowed = {
                                    if (!state.isCardFlipped) viewModel.nextPlayer()
                                },
                                front = { FaceCard(currentPlayer = state.currentPlayer!!) },
                                back = { BackCard() }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    val description = when (state.currentPlayer?.role) {
                        RoleType.MAFIA -> "Устраняйте игроков по ночам. Ваша цель — устранить всех мирных."
                        RoleType.CIVILIAN -> "Выживите и найдите мафию с помощью голосования."
                        RoleType.DOCTOR -> "Лечите одного игрока каждую ночь."
                        RoleType.DETECTIVE -> "Проверяйте игроков на принадлежность к мафии."
                        RoleType.MANIAC -> "Убивайте игроков. Все против вас."
                        RoleType.SLUT -> "Блокируйте действия других игроков ночью."
                        else -> "Описание недоступно."
                    }
                    AnimatedVisibility(visible = state.isCardFlipped) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.alpha(0.8f)
                            )

                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "Передайте следующему игроку",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.alpha(0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}


