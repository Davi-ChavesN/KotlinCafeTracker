package com.prova2.view.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prova2.controller.Screen
import com.prova2.model.Cafe
import com.prova2.model.CafeDAO
import com.prova2.view.components.CardItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var totalItems by remember { mutableStateOf(0) }
    var mostExpensiveCafe by remember { mutableStateOf<Cafe?>(null) }
    var averagePrice by remember { mutableStateOf(0.0) }
    var coffeeWithMostAroma by remember { mutableStateOf<Cafe?>(null) }
    var coffeeWithLeastAcidity by remember { mutableStateOf<Cafe?>(null) }

    val cafeDAO = CafeDAO()

    LaunchedEffect(Unit) {
        cafeDAO.buscarCafes(
            onDataChange = { cafes ->
                // Processar os dados após carregá-los
                totalItems = cafes.size

                mostExpensiveCafe = cafes.maxByOrNull { it.preco }
                averagePrice = cafes.map { it.preco }.average()
                coffeeWithMostAroma = cafes.maxByOrNull { it.aroma }
                coffeeWithLeastAcidity = cafes.minByOrNull { it.acidez }
            },
            onError = { error ->
                // Log de erro
                Log.e("HomeScreen", "Erro ao buscar cafés: ${error.message}")
            }
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.HomeScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(Icons.Default.Home, contentDescription = "Tela Inicial")
                    }
                    IconButton(onClick = {
                        navController.navigate(Screen.CafeListScreen.route) {
                            popUpTo(Screen.CafeListScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(Icons.Default.List, contentDescription = "Lista de Cafés")
                    }
                }
            }
        },
        drawerContent = {
            DrawerContent(
                navController = navController,
                scaffoldState = scaffoldState,
                scope = scope
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
// Total de Itens
            CardItem(title = "Total de Cafés", content = "$totalItems")

            // Café Mais Caro
            mostExpensiveCafe?.let {
                CardItem(
                    title = "Café Mais Caro",
                    content = "${it.nome} - R$ ${it.preco}"
                )
            }

            // Preço Médio
            CardItem(
                title = "Preço Médio",
                content = "R$ ${"%.2f".format(averagePrice)}"
            )

            // Café com Mais Aroma
            coffeeWithMostAroma?.let {
                CardItem(
                    title = "Café com Mais Aroma",
                    content = "${it.nome} - Aroma: ${it.aroma}"
                )
            }

            // Café com Menor Acidez
            coffeeWithLeastAcidity?.let {
                CardItem(
                    title = "Café com Menor Acidez",
                    content = "${it.nome} - Acidez: ${it.acidez}"
                )
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController, scaffoldState: ScaffoldState, scope : CoroutineScope) {
    val items = listOf("home_screen", "cafe_list_screen")//Nomes usados internamente.
    Column(modifier = Modifier.padding(16.dp)) {
        items.forEach { item ->
            Text(
                text = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        navController.navigate(item) {
                            popUpTo(item) {
                                inclusive = true
                            }
                        }
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
            )
        }
    }
}