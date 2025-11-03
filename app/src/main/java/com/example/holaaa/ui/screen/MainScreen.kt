package com.example.holaaa.ui.screen

import android.annotation.SuppressLint
import android.app.Application
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.holaaa.navigation.AppScreens
import com.example.holaaa.ui.viewmodel.AuthViewModel
import com.example.holaaa.ui.viewmodel.CartViewModel
import com.example.holaaa.ui.viewmodel.ProductListViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController, // NavController para la navegación principal (hacia Login)
    authViewModel: AuthViewModel // ViewModel de autenticación
) {
    val internalNavController = rememberNavController() // NavController para la navegación interna (BottomBar)

    // --- ViewModels ---
    val application = LocalContext.current.applicationContext as Application
    val productListViewModel: ProductListViewModel = viewModel(factory = ProductListViewModel.Factory(application))
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModel.Factory(application))

    // --- Estados de la UI ---
    val cartState by cartViewModel.cartUiState.collectAsState()
    val authState by authViewModel.uiState.collectAsState()
    val cartItemCount = cartState.items.sumOf { it.cantidad }

    val navBackStackEntry by internalNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Título dinámico basado en la pantalla actual y el estado de login
    val currentTitle = when (currentDestination?.route) {
        AppScreens.Inicio.route -> if (authState.isLoggedIn) authState.userEmail ?: "Inicio" else "Invitado"
        AppScreens.Compras.route -> "Todos los Productos"
        AppScreens.Favoritos.route -> "Favoritos"
        AppScreens.Cuenta.route -> "Mi Cuenta"
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
                            IconButton(onClick = { internalNavController.navigate(AppScreens.Cart.route) }) {
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
                    val items = listOf(BottomNavItem.Inicio, BottomNavItem.Compras, BottomNavItem.Favoritos, BottomNavItem.Cuenta)
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.screen_route } == true,
                            onClick = {
                                internalNavController.navigate(screen.screen_route) {
                                    popUpTo(internalNavController.graph.findStartDestination().id) { saveState = true }
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
            startDestination = AppScreens.Inicio.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppScreens.Inicio.route) { ProductListScreen(productListViewModel, internalNavController) }
            composable(AppScreens.Compras.route) { ShoppingScreen(productListViewModel, internalNavController) }
            composable(AppScreens.Favoritos.route) { WishlistScreen() }
            composable(AppScreens.Cuenta.route) { AccountScreen(navController, authViewModel) } // Pasa el AuthViewModel
            composable(AppScreens.Cart.route) { CartScreen(cartViewModel) }
            composable(AppScreens.ProductDetail.route) { ProductDetailScreen(internalNavController) }
        }
    }
}

sealed class BottomNavItem(var title: String, var icon: ImageVector, var screen_route: String) {
    object Inicio : BottomNavItem("Inicio", Icons.Default.Home, AppScreens.Inicio.route)
    object Compras : BottomNavItem("Compras", Icons.Default.ShoppingBag, AppScreens.Compras.route)
    object Favoritos : BottomNavItem("Favoritos", Icons.Default.Favorite, AppScreens.Favoritos.route)
    object Cuenta : BottomNavItem("Cuenta", Icons.Default.AccountCircle, AppScreens.Cuenta.route)
}