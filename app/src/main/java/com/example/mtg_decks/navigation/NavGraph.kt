package com.example.mtg_decks.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mtg_decks.ui.screens.CardDetailScreen
import com.example.mtg_decks.ui.screens.RandomCardScreen
import com.example.mtg_decks.ui.screens.SearchScreen

@Suppress("ktlint:standard:function-naming")
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
    ) {
        composable(Screen.Search.route) {
            SearchScreen(
                onCardClick = { cardId ->
                    navController.navigate(Screen.Detail.createRoute(cardId))
                },
            )
        }

        composable(Screen.Random.route) {
            RandomCardScreen(
                onCardClick = { cardId ->
                    navController.navigate(Screen.Detail.createRoute(cardId))
                },
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments =
                listOf(
                    navArgument("cardId") { type = NavType.StringType },
                ),
        ) {
            CardDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
