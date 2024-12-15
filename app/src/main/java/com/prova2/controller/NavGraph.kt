package com.prova2.controller

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prova2.view.screens.CafeAddScreen
import com.prova2.view.screens.CafeEditScreen
import com.prova2.view.screens.CafeListScreen
import com.prova2.view.screens.HomeScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(//Tela Principal
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(navController = navController)
        }
        composable(//Lista de Cafés
            route = Screen.CafeListScreen.route
        ) {
            CafeListScreen(navController = navController)
        }
        composable(//Adicionar Café
            route = Screen.CafeAddScreen.route
        ) {
            CafeAddScreen(navController = navController)
        }
        composable(//Editar Café
            route = Screen.CafeEditScreen.route + "/{cafeId}",
            arguments = listOf(navArgument("cafeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cafeId = backStackEntry.arguments?.getString("cafeId")
            CafeEditScreen(navController = navController, cafeId = cafeId)
        }
    }
}