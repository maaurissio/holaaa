package com.example.holaaa.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
    // Aquí iría tu servicio/repositorio de API (ej. Retrofit)
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
                // Si la cantidad es 0 o menos, elimina el item
                cartDao.deleteItem(item)
            } else {
                // Si no, actualiza la cantidad
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

                // 1. Crear el objeto Venta (ej. Venta(items = items, total = ...))
                //    val nuevaVenta = Venta(id = null, items = items, total = cartUiState.value.total)

                // 2. Enviar la venta al backend (Simulación con un delay)
                //    try {
                //        apiService.registrarVenta(nuevaVenta)
                //        Log.d("CartViewModel", "Venta registrada en el backend")
                //
                //        // 3. Si la venta es exitosa, limpiar el carro local
                //        cartDao.clearCart()
                //        onSaleRegistered() // Llama al callback para notificar a la UI
                //
                //    } catch (e: Exception) {
                //        Log.e("CartViewModel", "Error al registrar la venta", e)
                //        // (Manejar el error, quizás mostrar un Toast)
                //    }

                // --- INICIO SIMULACIÓN (Borra esto cuando tengas Retrofit) ---
                kotlinx.coroutines.delay(1000) // Simula la llamada a la red
                println("Venta simulada enviada al backend.")
                cartDao.clearCart()
                onSaleRegistered()
                // --- FIN SIMULACIÓN ---
            }
        }
    }
}

data class CartUiState(
    val items: List<ItemCarrito> = emptyList(),
    val total: Int = 0
)