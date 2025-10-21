package com.example.holaaa.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.holaaa.data.model.Producto
import com.example.holaaa.ui.viewmodel.ProductListViewModel

@Composable
fun ProductListScreen(
    productListViewModel: ProductListViewModel
) {
    val uiState by productListViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {

        // --- REQUISITO: Búsqueda ---
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { productListViewModel.onSearchQueryChange(it) },
            label = { Text("Buscar producto...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        // --- REQUISITO: Listado de Productos (con imágenes) ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columnas
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.products, key = { it.id }) { producto ->
                ProductCard(
                    producto = producto,
                    onAddToCart = {
                        productListViewModel.addToCart(it)
                        Toast.makeText(context, "${it.nombre} agregado", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

// Componente Reutilizable para la tarjeta de producto
@Composable
fun ProductCard(
    producto: Producto,
    onAddToCart: (Producto) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = producto.imagenUrl,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_background) // Placeholder
            )

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${producto.precio}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(32.dp) // Altura fija para alinear botones
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onAddToCart(producto) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar")
                }
            }
        }
    }
}