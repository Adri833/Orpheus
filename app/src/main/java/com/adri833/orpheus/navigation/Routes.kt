package com.adri833.orpheus.navigation

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Login : Routes("login")
    data object Home : Routes("home")
}