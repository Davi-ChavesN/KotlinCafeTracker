package com.prova2.controller

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object CafeListScreen : Screen("cafe_list_screen")
    object CafeAddScreen : Screen("cafe_add_screen")
    object CafeEditScreen : Screen("cafe_edit_screen")
}