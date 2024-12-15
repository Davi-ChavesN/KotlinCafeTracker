package com.prova2.view.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prova2.controller.Screen
import com.prova2.model.Cafe
import com.prova2.model.CafeDAO
import kotlinx.coroutines.launch

@Composable
fun CafeListScreen(
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var cafes by remember { mutableStateOf<List<Cafe>>(emptyList()) }
    var cafesOrdemNormal by remember { mutableStateOf<List<Cafe>>(emptyList()) }
    var isError by remember { mutableStateOf(false) }
    var isSortedBySabor by remember { mutableStateOf(false) }

    val cafeDAO = CafeDAO()

    fun loadCafes() {
        cafeDAO.buscarCafes(
            onDataChange = { fetchedCafes ->
                cafes = fetchedCafes.sortedBy { it.nome }
                isError = false
            },
            onError = { error ->
                Log.e("CafeListScreen", "Erro ao buscar cafés", error.toException())
                isError = true
            }
        )
    }

    cafesOrdemNormal = cafes

    LaunchedEffect(Unit) {
        loadCafes()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Lista de Cafés")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isSortedBySabor = !isSortedBySabor
                        cafes = if (isSortedBySabor) {
                            cafes.sortedBy { it.sabor }.asReversed()
                        } else {
                            cafes.sortedBy { it.nome }
                        }
                    }) {
                        Icon(Icons.Default.Star, contentDescription = "Ordenar por Sabor")
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.CafeAddScreen.route)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Café")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            // Exibir mensagem de erro caso haja um erro
            if (isError) {
                Text(
                    text = "Erro ao carregar cafés. Tente novamente mais tarde.",
                    color = MaterialTheme.colors.error,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }

            // Exibir lista de cafés
            if (cafes.isEmpty() && !isError) {
                Text("Nenhum café encontrado.", modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(cafes) { cafe ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    navController.navigate("${Screen.CafeEditScreen.route}/${cafe.id}")
                                },
                            elevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(text = "Nome: ${cafe.nome}", style = MaterialTheme.typography.h6)
                                Text(text = "ID: ${cafe.id}")
                                Text(text = "Preço: R$ ${cafe.preco}")
                            }
                        }
                    }
                }
            }
        }
    }
}