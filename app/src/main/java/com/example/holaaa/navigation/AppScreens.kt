package com.example.holaaa.navigation

sealed class AppScreens(val route: String) {
    object Login : AppScreens("login_screen")
    object MainApp : AppScreens("main_app_screen")

    // Pantallas del Bottom Nav
    object Inicio : AppScreens("inicio_screen")
    object Compras : AppScreens("compras_screen")
    object Favoritos : AppScreens("favoritos_screen")
    object Cuenta : AppScreens("cuenta_screen")

    // Otras pantallas
    object Cart : AppScreens("cart_screen")
    object ProductDetail : AppScreens("product_detail_screen")
}