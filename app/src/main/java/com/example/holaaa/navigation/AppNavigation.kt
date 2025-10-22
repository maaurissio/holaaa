package com.example.holaaa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.holaaa.ui.screen.LoginScreen
import com.example.holaaa.ui.screen.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Cambiamos la ruta inicial a la pantalla principal de la app
    NavHost(navController = navController, startDestination = AppScreens.MainApp.route) {

        composable(AppScreens.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(AppScreens.MainApp.route) {
            MainScreen()
        }
    }
}