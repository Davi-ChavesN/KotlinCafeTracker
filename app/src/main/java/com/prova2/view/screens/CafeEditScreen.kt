package com.prova2.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prova2.controller.Screen
import com.prova2.model.Cafe
import com.prova2.model.CafeDAO
import com.prova2.view.components.Botao
import com.prova2.view.components.CampoTexto
import com.prova2.view.components.CampoTextoNumero
import com.prova2.view.components.Nota
import com.prova2.view.components.NumberPicker
import kotlinx.coroutines.launch

@Composable
fun CafeEditScreen(
    navController: NavController,
    cafeId: String?
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var nome by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf(Nota.DOCE) }
    var aroma by remember { mutableStateOf(1) }
    var acidez by remember { mutableStateOf(1) }
    var amargor by remember { mutableStateOf(1) }
    var sabor by remember { mutableStateOf(1) }
    var preco by remember { mutableStateOf("") }

    var cafe by remember { mutableStateOf<Cafe?>(null) }
    val cafeDAO = CafeDAO()

    cafeId?.let {
        cafeDAO.buscarCafePorId(it, onComplete = { fetchedCafe ->
            cafe = fetchedCafe
            // Preencher os campos com os dados do café recuperado
            nome = cafe?.nome ?: ""
            nota = cafe?.nota ?: Nota.DOCE
            aroma = cafe?.aroma ?: 1
            acidez = cafe?.acidez ?: 1
            amargor = cafe?.amargor ?: 1
            sabor = cafe?.sabor ?: 1
            preco = cafe?.preco.toString()
        }, onError = { error ->
            Log.i("CafeEditScreen", "Erro ao buscar café", error.toException())
        })
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Editar Café")
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            cafe?.let {
                CampoTexto(
                    label = "Nome do Café",
                    value = nome,
                    onValueChange = { nome = it },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                var expanded by remember { mutableStateOf(false) }
                Column {
                    OutlinedButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(nota.descricao)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Nota.values().forEach { item ->
                            DropdownMenuItem(onClick = {
                                nota = item
                                expanded = false
                            }) {
                                Text(item.descricao)
                            }
                        }
                    }
                }

                listOf(
                    Triple("Aroma", aroma, { newValue: Int -> aroma = newValue }),
                    Triple("Acidez", acidez, { newValue: Int -> acidez = newValue }),
                    Triple("Amargor", amargor, { newValue: Int -> amargor = newValue }),
                    Triple("Sabor", sabor, { newValue: Int -> sabor = newValue })
                ).forEach { (label, value, onChange) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.h5.copy(
                                fontSize = 18.sp
                            )
                        )
                        NumberPicker(
                            value = value,
                            onValueChange = onChange
                        )
                    }
                }

                // Campo Preço
                CampoTextoNumero(
                    label = "Preço (R$)",
                    value = preco,
                    onValueChange = { preco = it },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Botão para Atualizar
                    OutlinedButton(
                        onClick = {
                            var cafe = Cafe(
                                cafe!!.id, nome, nota,
                                aroma, acidez, amargor, sabor,
                                preco.toDouble()
                            )
                            cafeDAO.atualizarCafe(cafe, onComplete = {})
                            navController.navigate(Screen.CafeListScreen.route) {
                                popUpTo(Screen.CafeListScreen.route) {
                                    inclusive = true
                                }
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color(0xFFE0E0E0)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Atualizar",
                            tint = Color.Black
                        )
                    }

                    // Botão para Deletar
                    OutlinedButton(
                        onClick = {
                            cafeDAO.excluirCafe(cafe!!.id, onComplete = {})
                            navController.navigate(Screen.CafeListScreen.route) {
                                popUpTo(Screen.CafeListScreen.route) {
                                    inclusive = true
                                }
                            }
                        },
                        shape = MaterialTheme.shapes.medium, // Bordas arredondadas
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color(0xFFEF9A9A)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Deletar",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}