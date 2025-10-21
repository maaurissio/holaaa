package com.example.holaaa.navigation

sealed class AppScreens(val route: String) {
    object Login : AppScreens("login_screen")
    object MainApp : AppScreens("main_app_screen") // Contenedor para el drawer

    // Pantallas internas de MainApp
    object ProductList : AppScreens("product_list_screen")
    object Cart : AppScreens("cart_screen")
    object Profile : AppScreens("profile_screen") // Ejemplo para el Drawer
}