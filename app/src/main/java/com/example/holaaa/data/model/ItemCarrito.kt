package com.example.holaaa.data.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito_items")
data class ItemCarrito(
    @PrimaryKey val productoId: String,
    val nombre: String,
    val precio: Int,
    @DrawableRes val imagenResId: Int,
    var cantidad: Int
)