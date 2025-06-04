package com.adri833.orpheus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adri833.orpheus.screens.login.LoginScreen
import com.adri833.orpheus.screens.splash.SplashScreen

@Composable
fun NavigationHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        // Navegacion de la pantalla Splash
        composable(route = Routes.Splash.route) {
            SplashScreen(
                navigationToLogin = {
                    navController.navigate(Routes.Login.route)
                }
            )
        }

        // Navegacion de la pantalla Login
        composable(route = Routes.Login.route) {
            LoginScreen()
        }

    }
}