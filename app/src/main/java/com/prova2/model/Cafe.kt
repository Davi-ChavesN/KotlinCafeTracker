package com.prova2.model

import com.prova2.view.components.Nota

data class Cafe(
    var id: String = "",
    var nome: String = "",
    var nota: Nota = Nota.DOCE,
    var aroma: Int = 0,
    var acidez: Int = 0,
    var amargor: Int = 0,
    var sabor: Int = 0,
    var preco: Double = 0.0
) {
    init {
        require(aroma in 0..5) { "Aroma deve estar entre 0 e 5" }
        require(acidez in 0..5) { "Acidez deve estar entre 0 e 5" }
        require(amargor in 0..5) { "Amargor deve estar entre 0 e 5" }
        require(sabor in 0..5) { "Sabor deve estar entre 0 e 5" }
    }
}

