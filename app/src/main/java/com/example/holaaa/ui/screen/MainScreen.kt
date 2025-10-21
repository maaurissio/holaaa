package com.example.holaaa.ui.screen

import android.annotation.SuppressLint
import android.app.Application // <-- Importar Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // <-- Importar viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.holaaa.navigation.AppScreens
import com.example.holaaa.ui.viewmodel.CartViewModel
import com.example.holaaa.ui.viewmodel.ProductListViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val internalNavController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // --- ¡ERROR CORREGIDO AQUÍ! ---
    // Obtenemos el contexto de la aplicación
    val application = LocalContext.current.applicationContext as Application

    // Usamos las 'Factories' para crear los ViewModels
    val productListViewModel: ProductListViewModel = viewModel(
        factory = ProductListViewModel.Factory(application)
    )
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModel.Factory(application)
    )
    // --- FIN DE LA CORRECCIÓN ---

    val cartState by cartViewModel.cartUiState.collectAsState()
    val cartItemCount = cartState.items.sumOf { it.cantidad }

    val navBackStackEntry by internalNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentTitle = when (currentRoute) {
        AppScreens.ProductList.route -> "Catálogo"
        AppScreens.Cart.route -> "Mi Carrito"
        AppScreens.Profile.route -> "Mi Perfil"
        else -> "Huerto Hogar"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onNavigate = { route ->
                    internalNavController.navigate(route) {
                        popUpTo(internalNavController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentTitle) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        titleContentColor = MaterialTheme.colorScheme.onTertiary,
                        actionIconContentColor = MaterialTheme.colorScheme.onTertiary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
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
        ) { paddingValues ->
            NavHost(
                navController = internalNavController,
                startDestination = AppScreens.ProductList.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(AppScreens.ProductList.route) {
                    ProductListScreen(
                        // Ahora solo pasamos el ViewModel
                        productListViewModel = productListViewModel
                    )
                }
                composable(AppScreens.Cart.route) {
                    CartScreen(
                        // Ahora solo pasamos el ViewModel
                        cartViewModel = cartViewModel
                    )
                }
                composable(AppScreens.Profile.route) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Pantalla de Perfil")
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet {
        Text("Huerto Hogar", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        Divider()
        DrawerItem(
            label = "Catálogo",
            icon = Icons.Default.List,
            onClick = { onNavigate(AppScreens.ProductList.route) }
        )
        DrawerItem(
            label = "Mi Perfil",
            icon = Icons.Default.Person,
            onClick = { onNavigate(AppScreens.Profile.route) }
        )
        DrawerItem(
            label = "Cerrar Sesión",
            icon = Icons.Default.ExitToApp,
            onClick = onLogout
        )
    }
}

@Composable
fun DrawerItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}