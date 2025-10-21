package com.example.holaaa.data.local

import androidx.room.*
import com.example.holaaa.data.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM carrito_items")
    fun getAllItems(): Flow<List<ItemCarrito>>

    @Query("SELECT * FROM carrito_items WHERE productoId = :id")
    suspend fun getItemById(id: String): ItemCarrito?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemCarrito)

    @Update
    suspend fun updateItem(item: ItemCarrito)

    @Delete
    suspend fun deleteItem(item: ItemCarrito)

    @Query("DELETE FROM carrito_items")
    suspend fun clearCart()
}