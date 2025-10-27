package com.example.holaaa.ui.screen

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.holaaa.navigation.AppScreens
import com.example.holaaa.ui.viewmodel.CartViewModel
import com.example.holaaa.ui.viewmodel.ProductListViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val internalNavController = rememberNavController()

    val application = LocalContext.current.applicationContext as Application
    val productListViewModel: ProductListViewModel = viewModel(
        factory = ProductListViewModel.Factory(application)
    )
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModel.Factory(application)
    )

    val cartState by cartViewModel.cartUiState.collectAsState()
    val cartItemCount = cartState.items.sumOf { it.cantidad }

    val navBackStackEntry by internalNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentTitle = when (currentDestination?.route) {
        AppScreens.Home.route -> "Invitado"
        AppScreens.Shopping.route -> "Todos los Productos"
        AppScreens.Wishlist.route -> "Favoritos"
        AppScreens.Account.route -> "Mi Cuenta"
        AppScreens.Cart.route -> "Mi Carrito"
        AppScreens.ProductDetail.route -> "Detalles"
        else -> "Huerto Hogar"
    }

    Scaffold(
        topBar = {
            if (currentDestination?.route != AppScreens.ProductDetail.route) {
                TopAppBar(
                    title = { Text(currentTitle) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    actions = {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge { Text("$cartItemCount") }
                                }
                            }
                        ) {
                            IconButton(onClick = {
                                internalNavController.navigate(AppScreens.Cart.route)
                            }) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentDestination?.route != AppScreens.ProductDetail.route) {
                NavigationBar {
                    val items = listOf(
                        BottomNavItem.Home,
                        BottomNavItem.Shopping,
                        BottomNavItem.Wishlist,
                        BottomNavItem.Account
                    )
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.screen_route } == true,
                            onClick = {
                                internalNavController.navigate(screen.screen_route) {
                                    popUpTo(internalNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = internalNavController,
            startDestination = AppScreens.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppScreens.Home.route) {
                ProductListScreen(
                    productListViewModel = productListViewModel,
                    navController = internalNavController
                )
            }
            composable(AppScreens.Shopping.route) { 
                 ShoppingScreen(
                    productListViewModel = productListViewModel,
                    navController = internalNavController
                )
            }
            composable(AppScreens.Wishlist.route) { WishlistScreen() }
            composable(AppScreens.Account.route) { AccountScreen() }
            composable(AppScreens.Cart.route) {
                CartScreen(cartViewModel = cartViewModel)
            }
            composable(AppScreens.ProductDetail.route) {
                ProductDetailScreen(navController = internalNavController)
            }
        }
    }
}

sealed class BottomNavItem(var title: String, var icon: ImageVector, var screen_route: String) {
    object Home : BottomNavItem("Home", Icons.Default.Home, AppScreens.Home.route)
    object Shopping : BottomNavItem("Shopping", Icons.Default.ShoppingBag, AppScreens.Shopping.route)
    object Wishlist : BottomNavItem("Wishlist", Icons.Default.Favorite, AppScreens.Wishlist.route)
    object Account : BottomNavItem("Account", Icons.Default.AccountCircle, AppScreens.Account.route)
}