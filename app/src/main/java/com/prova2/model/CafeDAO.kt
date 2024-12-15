package com.prova2.model

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CafeDAO() {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("cafes")

    // Adicionar um café ao banco
    fun adicionarCafe(cafe: Cafe, onComplete: (Boolean) -> Unit) {
        val cafeId = database.push().key ?: return onComplete(false)
        val cafeComId = cafe.copy(id = cafeId) // Adiciona o ID gerado ao objeto
        database.child(cafeId).setValue(cafeComId).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    // Atualizar os dados de um café
    fun atualizarCafe(cafe: Cafe, onComplete: (Boolean) -> Unit) {
        if (cafe.id.isNotEmpty()) {
            database.child(cafe.id).setValue(cafe).addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
        } else {
            onComplete(false)
        }
    }

    // Excluir um café pelo ID
    fun excluirCafe(cafeId: String, onComplete: (Boolean) -> Unit) {
        database.child(cafeId).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    // Buscar todos os cafés
    fun buscarCafes(onDataChange: (List<Cafe>) -> Unit, onError: (DatabaseError) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cafes = mutableListOf<Cafe>()
                for (data in snapshot.children) {
                    val cafe = data.getValue(Cafe::class.java)
                    cafe?.let { cafes.add(it) }
                }
                onDataChange(cafes)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }

    // Buscar um café por ID
    fun buscarCafePorId(cafeId: String, onComplete: (Cafe?) -> Unit, onError: (DatabaseError) -> Unit) {
        database.child(cafeId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cafe = snapshot.getValue(Cafe::class.java)
                onComplete(cafe)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }
}