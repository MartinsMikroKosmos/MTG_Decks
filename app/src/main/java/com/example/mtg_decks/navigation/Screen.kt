package com.example.mtg_decks.navigation

sealed class Screen(
    val route: String,
) {
    object Search : Screen("search")

    object Random : Screen("random")

    object Detail : Screen("detail/{cardId}") {
        fun createRoute(cardId: String) = "detail/$cardId"
    }
}
