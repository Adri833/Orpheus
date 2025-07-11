package com.adri833.orpheus.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.adri833.orpheus.components.BottomBar
import com.adri833.orpheus.screens.downloader.DownloaderScreen
import com.adri833.orpheus.screens.drive.DriveScreen
import com.adri833.orpheus.screens.home.HomeScreen
import com.adri833.orpheus.screens.library.LibraryScreen
import com.adri833.orpheus.screens.login.LoginScreen
import com.adri833.orpheus.screens.splash.SplashScreen
import com.adri833.orpheus.utils.adjustForMobile
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adri833.orpheus.components.NowPlayingBar
import com.adri833.orpheus.screens.player.PlayerViewModel
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import com.adri833.orpheus.components.PlaybackQueueBottomSheet
import com.adri833.orpheus.screens.song.SongScreen
import com.adri833.orpheus.ui.slideInUpAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val showNowPlayingBar = currentRoute != Routes.Song.route
    var showBars by remember { mutableStateOf(true) }

    LaunchedEffect(currentRoute) {
        showBars = currentRoute != Routes.Song.route
    }

    val showBottomBar = currentRoute in listOf(
        Routes.Home.route,
        Routes.Playlist.route,
        Routes.Drive.route,
        Routes.Downloader.route,
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showQueue by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            Column(modifier = Modifier.adjustForMobile()) {
                val currentSong by playerViewModel.currentSong.collectAsState()

                AnimatedVisibility(
                    visible = showBars && showNowPlayingBar && currentSong != null,
                    enter = slideInUpAnimation(400),
                ) {
                    NowPlayingBar(
                        viewModel = playerViewModel,
                        onQueueClick = { showQueue = true },
                        navigationToSong = {
                            coroutineScope.launch {
                                showBars = false
                                delay(250)
                                navController.navigate(Routes.Song.route)
                            }
                        }
                    )
                }

                AnimatedVisibility(
                    visible = showBars && showBottomBar,
                    enter = slideInUpAnimation(400),
                ) {
                    BottomBar(navController)
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
                HomeScreen(playerViewModel)
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

            // Navegacion de la pantalla Song
            composable(Routes.Song.route) {
                SongScreen(
                    viewModel = playerViewModel,
                    onBack = { navController.popBackStack() })
            }
        }
    }
    if (showQueue) {
        androidx.compose.material3.ModalBottomSheet(
            onDismissRequest = { showQueue = false },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        ) {
            PlaybackQueueBottomSheet(
                playerViewModel = playerViewModel,
                onDismissRequest = { showQueue = false }
            )
        }
    }
}