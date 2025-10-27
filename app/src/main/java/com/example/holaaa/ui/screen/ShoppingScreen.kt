package com.example.holaaa.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.holaaa.navigation.AppScreens
import com.example.holaaa.ui.viewmodel.ProductListViewModel

@Composable
fun ShoppingScreen(
    productListViewModel: ProductListViewModel,
    navController: NavController
) {
    val uiState by productListViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        // Barra de búsqueda
        TextField(
            value = uiState.searchQuery,
            onValueChange = { productListViewModel.onSearchQueryChange(it) },
            placeholder = { Text("Buscar producto...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(30.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                disabledContainerColor = Color.LightGray.copy(alpha = 0.2f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )

        // Cuadrícula de todos los productos
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.products, key = { it.id }) { producto ->
                ProductCard(
                    producto = producto,
                    onAddToCart = {
                        productListViewModel.addToCart(it)
                        Toast.makeText(context, "${it.nombre} agregado", Toast.LENGTH_SHORT).show()
                    },
                    onFavoriteClick = { /* Lógica de favoritos */ },
                    onCardClick = { navController.navigate(AppScreens.ProductDetail.route) }
                )
            }
        }
    }
}
