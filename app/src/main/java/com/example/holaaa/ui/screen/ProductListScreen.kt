package com.example.holaaa.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.holaaa.data.model.Producto
import com.example.holaaa.navigation.AppScreens
import com.example.holaaa.ui.viewmodel.ProductListViewModel

// --- Datos de ejemplo de Huerto Hogar ---
val categoriasHuertoHogar = listOf(
    "Frutas Frescas",
    "Verduras Orgánicas",
    "Productos Orgánicos",
    "Productos Lácteos"
)

val productosHuertoHogar = mapOf(
    "Frutas Frescas" to Producto(id = "FR001", nombre = "Manzanas Fuji", precio = 1200, stock = 150, descripcion = "Manzanas Fuji crujientes y dulces...", imagenUrl = ""),
    "Verduras Orgánicas" to Producto(id = "VR001", nombre = "Zanahorias Orgánicas", precio = 900, stock = 100, descripcion = "Zanahorias crujientes cultivadas sin pesticidas...", imagenUrl = ""),
    "Productos Orgánicos" to Producto(id = "PO001", nombre = "Miel Orgánica", precio = 5000, stock = 50, descripcion = "Miel pura y orgánica producida por apicultores locales...", imagenUrl = ""),
    "Productos Lácteos" to Producto(id = "PL001", nombre = "Leche Entera", precio = 1000, stock = 80, descripcion = "Leche fresca de granjas locales...", imagenUrl = "")
)
// ----------------------------------------

@Composable
fun ProductListScreen(
    productListViewModel: ProductListViewModel,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }

        items(categoriasHuertoHogar) { categoria ->
            val producto = productosHuertoHogar[categoria]
            if (producto != null) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(categoria, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        TextButton(onClick = { navController.navigate(AppScreens.Shopping.route) }) {
                            Text("Ver todos", color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    ProductCard(
                        producto = producto,
                        onAddToCart = { productListViewModel.addToCart(it) },
                        onFavoriteClick = { /* Lógica de favoritos */ },
                        onCardClick = { navController.navigate(AppScreens.ProductDetail.route) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    producto: Producto,
    onAddToCart: (Producto) -> Unit,
    onFavoriteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.clickable(onClick = onCardClick)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.background) // Placeholder color
            ) {
                if (producto.imagenUrl.isNotEmpty()) {
                    AsyncImage(
                        model = producto.imagenUrl,
                        contentDescription = producto.nombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorite", tint = MaterialTheme.colorScheme.onSurface)
                }
            }

            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", producto.precio.toDouble())}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(
                        onClick = {
                            onAddToCart(producto)
                            Toast.makeText(context, "${producto.nombre} agregado", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Add to Cart",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}