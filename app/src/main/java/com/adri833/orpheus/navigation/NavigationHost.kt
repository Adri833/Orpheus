package com.adri833.orpheus.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.adri833.orpheus.components.BottomBar
import com.adri833.orpheus.screens.downloader.DownloaderScreen
import com.adri833.orpheus.screens.drive.DriveScreen
import com.adri833.orpheus.screens.home.HomeScreen
import com.adri833.orpheus.screens.library.LibraryScreen
import com.adri833.orpheus.screens.login.LoginScreen
import com.adri833.orpheus.screens.search.SearchScreen
import com.adri833.orpheus.screens.splash.SplashScreen
import com.adri833.orpheus.utils.adjustForMobile
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Routes.Home.route,
        Routes.Search.route,
        Routes.Playlist.route,
        Routes.Drive.route,
        Routes.Downloader.route
    )

    Scaffold(
        bottomBar = {
            Box(modifier = Modifier.adjustForMobile()) {
                AnimatedVisibility(
                    visible = showBottomBar,
                    enter = fadeIn(tween(1000)),
                ) {
                    BottomBar(navController)
                }

                if (!showBottomBar) {
                    Box(modifier = Modifier
                        .height(65.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            // Navegacion de la pantalla Splash
            composable(
                route = Routes.Splash.route,
            ) {
                SplashScreen(
                    navigationToLogin = {
                        navController.navigate(Routes.Login.route) {
                            popUpTo(Routes.Splash.route) { inclusive = true }
                        }
                    },
                    navigationToHome = {
                        navController.navigate(Routes.Home.route) {
                            popUpTo(Routes.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // Navegacion de la pantalla Login
            composable(
                route = Routes.Login.route,
            ) {
                LoginScreen(
                    navigationToHome = {
                        navController.navigate(Routes.Home.route) {
                            popUpTo(Routes.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // Navegacion de la pantalla Home
            composable(
                route = Routes.Home.route,
            ) {
                HomeScreen()
            }

            // Navegacion de la pantalla Search
            composable(Routes.Search.route) {
                SearchScreen()
            }

            // Navegacion de la pantalla Library
            composable(Routes.Playlist.route) {
                LibraryScreen()
            }

            // Navegacion de la pantalla Drive
            composable(Routes.Drive.route) {
                DriveScreen()
            }

            // Navegacion de la pantalla Downloader
            composable(Routes.Downloader.route) {
                DownloaderScreen()
            }
        }
    }
}