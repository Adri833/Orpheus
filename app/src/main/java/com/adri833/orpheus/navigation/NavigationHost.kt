@file:Suppress("DEPRECATION")

package com.adri833.orpheus.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.adri833.orpheus.screens.home.HomeScreen
import com.adri833.orpheus.screens.login.LoginScreen
import com.adri833.orpheus.screens.splash.SplashScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Routes.Home.route,
    ) {

        // Navegacion de la pantalla Splash
        composable(route = Routes.Splash.route) {
            SplashScreen(
                navigationToLogin = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Navegacion de la pantalla Login
        composable(route = Routes.Login.route) {
            LoginScreen(
                navigationToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Navegacion de la pantalla Home
        composable(route = Routes.Home.route) {
            HomeScreen()
        }
    }
}