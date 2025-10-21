package com.example.holaaa.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.holaaa.data.local.AppDatabase
import com.example.holaaa.data.model.ItemCarrito
import com.example.holaaa.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Usamos AndroidViewModel para acceder a la BD
class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    private val cartDao = AppDatabase.getDatabase(application).cartDao()

    // Lista de productos simulada (basada en el PDF y proyecto React)
    private val _allProducts = listOf(
        Producto("VR001", "Zanahorias", "Frescas y orgánicas de O'Higgins", 1200, 100, "https://i.imgur.com/wS22N9s.jpeg"),
        Producto("VR002", "Espinacas Frescas", "Bolsa de 500g, orgánicas", 700, 80, "https://i.imgur.com/pXQ4nZg.jpeg"),
        Producto("VR003", "Pimientos Tricolores", "Kilo de pimientos (rojo, amarillo, verde)", 1500, 120, "https://i.imgur.com/pZqY3Wl.jpeg"),
        Producto("PO001", "Miel Orgánica", "Frasco de 500g, apicultores locales", 5000, 50, "https://i.imgur.com/1aA2y9U.jpeg")
        // Puedes agregar más productos aquí
    )

    private val _uiState = MutableStateFlow(ProductListUiState(products = _allProducts))
    val uiState = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterProducts()
    }

    private fun filterProducts() {
        val query = uiState.value.searchQuery
        _uiState.update {
            it.copy(
                products = if (query.isEmpty()) {
                    _allProducts
                } else {
                    _allProducts.filter { producto ->
                        producto.nombre.contains(query, ignoreCase = true)
                    }
                }
            )
        }
    }

    fun addToCart(producto: Producto) {
        viewModelScope.launch {
            // Revisa si el item ya existe
            val existingItem = cartDao.getItemById(producto.id)

            if (existingItem != null) {
                // Si existe, solo actualiza la cantidad
                val updatedItem = existingItem.copy(cantidad = existingItem.cantidad + 1)
                cartDao.updateItem(updatedItem)
            } else {
                // Si no existe, crea un nuevo item
                val newItem = ItemCarrito(
                    productoId = producto.id,
                    nombre = producto.nombre,
                    precio = producto.precio,
                    imagenUrl = producto.imagenUrl,
                    cantidad = 1
                )
                cartDao.insertItem(newItem)
            }
        }
    }

    // --- ¡ERROR CORREGIDO AQUÍ! ---
    // Esta "Factory" le dice a Compose cómo crear un ViewModel que necesita "Application"
    companion object {
        val Factory: (Application) -> ViewModelProvider.Factory = { application ->
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
                        return ProductListViewModel(application) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}

data class ProductListUiState(
    val products: List<Producto> = emptyList(),
    val searchQuery: String = ""
)