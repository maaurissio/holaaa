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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

const val productName = "Cotton T-Shirt"
const val productCategory = "Outerwear Men"
const val productPrice = 86.00
const val productDescription = "A cotton t-shirt is a must-have for its softness, breathability, and effortless style. Ideal for cool in warm weather and adds a light layer when needed. With a range of colors..."

@Composable
fun ProductDetailScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedImageIndex by remember { mutableStateOf(0) }
    val imageCount = 4 // Number of placeholder images
    var selectedSize by remember { mutableStateOf("L") }
    val sizes = listOf("S", "M", "L", "XL")
    var quantity by remember { mutableStateOf(1) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {
            // Main product image placeholder
            Box(
                modifier = Modifier.fillMaxSize().background(Color.LightGray)
            )
            // Top buttons
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
            // Favorite button
            IconButton(
                onClick = { /* Favorite action */ },
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 60.dp, end = 16.dp)
            ) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.Black)
            }
            // Image gallery
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
                                    color = if (selectedImageIndex == index) Color(0xFFFF5722) else Color.Transparent,
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
                Text(productName, fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.weight(1f))
                Text("$${String.format("%.2f", productPrice)}", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            Text(productCategory, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Size & Quantity
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Select Size", fontWeight = FontWeight.Medium, fontSize = 16.sp, modifier = Modifier.weight(1f))
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
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sizes.size) { index ->
                    val size = sizes[index]
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (selectedSize == size) Color(0xFFFF5722) else Color.LightGray.copy(0.2f))
                            .clickable { selectedSize = size },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(size, color = if (selectedSize == size) Color.White else Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text("Description", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(productDescription, color = Color.Gray, fontSize = 14.sp, maxLines = 3, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
            Text("Learn More", color = Color(0xFFFF5722), fontWeight = FontWeight.Bold, modifier = Modifier.clickable { /* Navigate to full description */ })
            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = "Add to Cart")
                }
                Button(
                    onClick = { Toast.makeText(context, "Buying now!", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.weight(1f).height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                ) {
                    Text("Buy Now", fontSize = 18.sp)
                }
            }
        }
    }
}