package com.example.holaaa.data.model

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val stock: Int,
    val imagenUrl: String
)