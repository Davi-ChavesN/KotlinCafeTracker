package com.prova2.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ------------------------ BUTTONS ------------------------
@Composable
fun Botao(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = text)
    }
}

// ------------------------ TEXT FIELDS ------------------------
@Composable
fun CampoTexto(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier
    )
}

@Composable
fun CampoTextoNumero(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
    )
}

// ------------------------ PICKER ------------------------
@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange = 1..5
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        range.forEach { number ->
            Text(
                text = number.toString(),
                modifier = Modifier
                    .clickable { onValueChange(number) }
                    .padding(8.dp), // Mais espaçamento para maior área de clique
                color = if (number == value) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h5.copy( // Aumenta o tamanho da fonte
                    fontWeight = if (number == value) FontWeight.Bold else FontWeight.Normal, // Destacar o número selecionado
                    fontSize = 20.sp // Tamanho de fonte maior
                )
            )
        }
    }
}