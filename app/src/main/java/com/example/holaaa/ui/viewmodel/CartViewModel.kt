package com.example.holaaa.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.holaaa.data.local.AppDatabase
import com.example.holaaa.data.model.ItemCarrito
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val cartDao = AppDatabase.getDatabase(application).cartDao()
    // private val apiService = ApiService.create()

    val cartUiState: StateFlow<CartUiState> = cartDao.getAllItems()
        .map { items ->
            val total = items.sumOf { it.precio * it.cantidad }
            CartUiState(items = items, total = total)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState()
        )

    fun updateQuantity(item: ItemCarrito, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                cartDao.deleteItem(item)
            } else {
                cartDao.updateItem(item.copy(cantidad = newQuantity))
            }
        }
    }

    fun deleteItem(item: ItemCarrito) {
        viewModelScope.launch {
            cartDao.deleteItem(item)
        }
    }

    fun registerSale(onSaleRegistered: () -> Unit) {
        viewModelScope.launch {
            val items = cartUiState.value.items
            if (items.isNotEmpty()) {

                // --- INICIO SIMULACIÓN (Borra esto cuando tengas Retrofit) ---
                kotlinx.coroutines.delay(1000) // Simula la llamada a la red
                println("Venta simulada enviada al backend.")
                cartDao.clearCart()
                onSaleRegistered()
                // --- FIN SIMULACIÓN ---
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
                    if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                        return CartViewModel(application) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}

data class CartUiState(
    val items: List<ItemCarrito> = emptyList(),
    val total: Int = 0
)