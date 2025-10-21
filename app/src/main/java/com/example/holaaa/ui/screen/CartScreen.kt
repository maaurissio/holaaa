package com.example.holaaa.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.holaaa.data.model.ItemCarrito
import com.example.holaaa.ui.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel
) {
    val uiState by cartViewModel.cartUiState.collectAsState()
    val context = LocalContext.current

    Column(Modifier.fillMaxSize()) {
        if (uiState.items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            // --- REQUISITO: Listado de Carro ---
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.items, key = { it.productoId }) { item ->

                    // --- REQUISITO: Eliminar producto mediante desplazar ---
                    val dismissState = rememberDismissState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == DismissValue.DismissedToEnd || dismissValue == DismissValue.DismissedToStart) {
                                cartViewModel.deleteItem(item)
                                true // Confirmamos la eliminación
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            val color = when(dismissState.targetValue) {
                                DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.errorContainer
                                DismissValue.DismissedToStart -> MaterialTheme.colorScheme.errorContainer
                                else -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterStart // o CenterEnd
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    ) {
                        // Fila del item
                        CartItemRow(
                            item = item,
                            onQuantityChange = { newQuantity ->
                                // --- REQUISITO: Modificación de carro de compra ---
                                cartViewModel.updateQuantity(item, newQuantity)
                            }
                        )
                    }
                }
            }

            // --- Total y Botón de Pago ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Total: $${uiState.total}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // --- REQUISITO: Enviar la venta al backend ---
                        cartViewModel.registerSale(onSaleRegistered = {
                            Toast.makeText(context, "Venta registrada con éxito", Toast.LENGTH_LONG).show()
                            // La navegación (si la hubiera) se manejaría aquí
                        })
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Enviar Venta", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

// Componente Reutilizable para la fila del carrito
@Composable
fun CartItemRow(
    item: ItemCarrito,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.nombre,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$${item.precio}", style = MaterialTheme.typography.bodyMedium)
            }

            // --- REQUISITO: Modificación de carro de compra ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onQuantityChange(item.cantidad - 1) }) {
                    Text("-", style = MaterialTheme.typography.titleLarge)
                }
                Text("${item.cantidad}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.width(24.dp))
                IconButton(onClick = { onQuantityChange(item.cantidad + 1) }) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}