package com.adri833.orpheus.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.adri833.orpheus.R
import com.adri833.orpheus.navigation.Routes

data class BottomNavItem(
    val label: String,
    val filledIcon: Painter,
    val outlinedIcon: Painter,
    val route: String
)

object BottomNavItemsProvider {
    @Composable
    fun getBottomNavItems(): List<BottomNavItem> {
        return listOf(
            BottomNavItem(
                label = "Home",
                filledIcon = painterResource(id = R.drawable.ic_home_fill),
                outlinedIcon = painterResource(id = R.drawable.ic_home_outlined),
                route = Routes.Home.route
            ),
            BottomNavItem(
                label = "Search",
                filledIcon = painterResource(id = R.drawable.ic_search_fill),
                outlinedIcon = painterResource(id = R.drawable.ic_search_outlined),
                route = Routes.Search.route
            ),
            BottomNavItem(
                label = "Library",
                filledIcon = painterResource(id = R.drawable.ic_playlist_fill),
                outlinedIcon = painterResource(id = R.drawable.ic_playlist_outlined),
                route = Routes.Library.route
            ),
            BottomNavItem(
                label = "Drive",
                filledIcon = painterResource(id = R.drawable.ic_drive_fill),
                outlinedIcon = painterResource(id = R.drawable.ic_drive_outlined),
                route = Routes.Drive.route
            ),
            BottomNavItem(
                label = "Downloader",
                filledIcon = painterResource(id = R.drawable.ic_youtube_fill),
                outlinedIcon = painterResource(id = R.drawable.ic_youtube_outlined),
                route = Routes.Downloader.route
            )
        )
    }
}
