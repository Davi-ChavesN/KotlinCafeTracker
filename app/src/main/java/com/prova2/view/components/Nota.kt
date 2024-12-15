package com.prova2.view.components

enum class Nota(val descricao: String) {
    DOCE("Doce"),
    FLORAL("Floral"),
    FRUTADO("Frutado"),
    ESPECIARIAS("Especiarias");

    override fun toString(): String {
        return descricao
    }
}