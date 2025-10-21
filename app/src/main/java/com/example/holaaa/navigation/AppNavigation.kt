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

    NavHost(navController = navController, startDestination = AppScreens.Login.route) {

        composable(AppScreens.Login.route) {
            LoginScreen(navController = navController)
        }

        // MainScreen contendrá el Drawer, Navbar y el NavHost interno
        composable(AppScreens.MainApp.route) {
            MainScreen()
        }
    }
}