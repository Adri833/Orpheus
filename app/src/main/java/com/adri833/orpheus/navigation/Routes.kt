package com.adri833.orpheus.navigation

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Login : Routes("login")
    data object Home : Routes("home")
    data object Search : Routes("search")
    data object Playlist : Routes("playlist")
    data object Drive : Routes("drive")
    data object Downloader : Routes("downloader")
}