package com.example.holaaa.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    if (uiState.items.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Tu carrito está vacío", style = MaterialTheme.typography.titleLarge)
        }
        return
    }

    var discountCode by remember { mutableStateOf("") }
    val discount = 25.0 // Hardcoded for design

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.items, key = { it.productoId }) { item ->
                CartItemRow(
                    item = item,
                    onQuantityChange = { newQuantity ->
                        cartViewModel.updateQuantity(item, newQuantity)
                    },
                    onDeleteItem = { cartViewModel.deleteItem(item) }
                )
            }
        }

        // Checkout Section
        CheckoutSection(
            subtotal = uiState.total.toDouble(),
            discount = discount,
            discountCode = discountCode,
            onDiscountCodeChange = { discountCode = it },
            onApplyDiscount = {
                Toast.makeText(context, "Descuento aplicado", Toast.LENGTH_SHORT).show()
            },
            onCheckout = {
                cartViewModel.registerSale(onSaleRegistered = {
                    Toast.makeText(context, "Venta registrada con éxito", Toast.LENGTH_LONG).show()
                })
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItemRow(
    item: ItemCarrito,
    onQuantityChange: (Int) -> Unit,
    onDeleteItem: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDeleteItem()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = when (dismissState.targetValue) {
                SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF5722).copy(alpha = 0.8f)
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
            }
        }
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.imagenUrl,
                    contentDescription = item.nombre,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("$${String.format("%.2f", item.precio.toDouble())}", color = Color.Gray, fontSize = 14.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuantityButton(text = "-") { onQuantityChange(item.cantidad - 1) }
                    Text("${item.cantidad}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    QuantityButton(text = "+") { onQuantityChange(item.cantidad + 1) }
                }
            }
        }
    }
}

@Composable
fun QuantityButton(text: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(30.dp).background(Color(0xFFFF5722), CircleShape)
    ) {
        Text(text, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CheckoutSection(
    subtotal: Double,
    discount: Double,
    discountCode: String,
    onDiscountCodeChange: (String) -> Unit,
    onApplyDiscount: () -> Unit,
    onCheckout: () -> Unit
) {
    val total = subtotal - discount

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = discountCode,
            onValueChange = onDiscountCodeChange,
            label = { Text("Enter Discount Code") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                TextButton(onClick = onApplyDiscount) {
                    Text("Apply", color = Color(0xFFFF5722))
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Sub total:", color = Color.Gray)
            Text("$${String.format("%.2f", subtotal)}", fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Discount:", color = Color.Gray)
            Text("-$${String.format("%.2f", discount)}", fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("$${String.format("%.2f", total)}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onCheckout,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
        ) {
            Text("Checkout", modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}
