package com.example.holaaa.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.holaaa.data.model.Producto

@Composable
fun ProductDetailScreen(navController: NavController) {
    // Usamos un producto de ejemplo de Huerto Hogar
    val producto = productosHuertoHogar["Frutas Frescas"]!!
    val context = LocalContext.current
    var selectedImageIndex by remember { mutableStateOf(0) }
    val imageCount = 4
    var quantity by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()) // Hacemos la columna scrollable
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {
            Box(modifier = Modifier.fillMaxSize().background(Color.LightGray))

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
                IconButton(onClick = { /* Share action */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Black)
                }
            }

            IconButton(
                onClick = { /* Favorite action */ },
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 60.dp, end = 16.dp)
            ) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.Black)
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
            ) {
                LazyRow(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(imageCount) { index ->
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Gray)
                                .clickable { selectedImageIndex = index }
                                .border(
                                    width = if (selectedImageIndex == index) 2.dp else 0.dp,
                                    color = if (selectedImageIndex == index) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        )
                    }
                }
            }
        }

        // Product Info
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.weight(1f))
                Text("$${producto.precio}", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            Text("Stock: ${producto.stock} disponibles", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Quantity Selector
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Cantidad", fontWeight = FontWeight.Medium, fontSize = 16.sp, modifier = Modifier.weight(1f))
                Row {
                    IconButton(onClick = { if (quantity > 1) quantity-- }) {
                        Text("-", fontSize = 20.sp)
                    }
                    Text("$quantity", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterVertically))
                    IconButton(onClick = { quantity++ }) {
                        Text("+", fontSize = 20.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text("Descripción", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(producto.descripcion, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = "Add to Cart")
                }
                Button(
                    onClick = { Toast.makeText(context, "Comprando ahora!", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.weight(1f).height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Comprar Ahora", fontSize = 18.sp)
                }
            }
        }
    }
}