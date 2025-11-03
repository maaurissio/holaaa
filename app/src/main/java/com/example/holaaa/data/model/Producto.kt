package com.example.holaaa.data.model

import androidx.annotation.DrawableRes

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val stock: Int,
    @DrawableRes val imagenResId: Int
)