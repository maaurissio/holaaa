package com.example.holaaa.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito_items")
data class ItemCarrito(
    @PrimaryKey val productoId: String,
    val nombre: String,
    val precio: Int,
    val imagenUrl: String,
    var cantidad: Int
)