package com.yakovskij.mafiacards

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yakovskij.mafiacards.features.howtoplay.presentation.localgame.HowToPlayScreen
import com.yakovskij.mafiacards.features.loader.LoadingScreen
import com.yakovskij.mafiacards.features.localgame.presentation.game.GameScreen
import com.yakovskij.mafiacards.features.localgame.presentation.setupDeck.DeckScreen
import com.yakovskij.mafiacards.features.localgame.presentation.setupGame.SetupGameScreen
import com.yakovskij.mafiacards.features.localgame.presentation.setupPlayers.PlayerSetupScreen
import com.yakovskij.mafiacards.features.mainmenu.presentation.mainmenu.MainMenuScreen

sealed class Screen(val route: String) {
    object Loader : Screen("loader")
    object MainMenu : Screen("main_menu")
    object LocalGameSetup : Screen("local_game_setup")
    object LocalGameSetupDeck : Screen("local_game_setup_deck")
    object LocalGameSetupPlayers : Screen("local_game_setup_players")
    object LocalGameScreen : Screen("local_game")
    object HowToPlay : Screen("how_to_play")
}

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String = Screen.Loader.route) {
    NavHost(navController, startDestination = startDestination) {

        composable(Screen.Loader.route) {
            LoadingScreen(
                onLoaded = {
                    navController.navigate(Screen.MainMenu.route) {
                        popUpTo(Screen.Loader.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                onCreateGame = {
                    navController.navigate(Screen.LocalGameSetup.route)
                },
                onStats = {},
                onHowToPlay = {
                    navController.navigate(Screen.HowToPlay.route)
                }
            )
        }
        composable(Screen.LocalGameSetup.route) {
            SetupGameScreen (
                onStartClick = {
                    navController.navigate(Screen.LocalGameScreen.route)
                },
                onDeckClick = {
                    navController.navigate(Screen.LocalGameSetupDeck.route)
                },
                onUsersClick = {
                    navController.navigate(Screen.LocalGameSetupPlayers.route)
                }

            )
        }
        composable(Screen.LocalGameSetupPlayers.route) {
            PlayerSetupScreen(onBack = {
                navController.popBackStack()
            })
        }
        composable(Screen.LocalGameSetupDeck.route) {
            DeckScreen(onBack = {
                navController.popBackStack()
            })
        }
        composable(Screen.LocalGameScreen.route) {
            GameScreen(
                onExitConfirmed = {
                    navController.popBackStack(Screen.LocalGameSetup.route, false)
                }
            )
        }

        composable(Screen.HowToPlay.route) {
            HowToPlayScreen(
                onFinish = {
                    navController.popBackStack(Screen.MainMenu.route, false)
                }
            )
        }
    }
}
