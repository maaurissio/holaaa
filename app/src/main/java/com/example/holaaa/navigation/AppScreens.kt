package com.example.holaaa.navigation

sealed class AppScreens(val route: String) {
    object Login : AppScreens("login_screen")
    object MainApp : AppScreens("main_app_screen")

    // Pantallas del Bottom Nav
    object Home : AppScreens("home_screen")
    object Shopping : AppScreens("shopping_screen")
    object Wishlist : AppScreens("wishlist_screen")
    object Account : AppScreens("account_screen")

    // Otras pantallas
    object Cart : AppScreens("cart_screen")
    object ProductDetail : AppScreens("product_detail_screen")
}
