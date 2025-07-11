package com.adri833.orpheus.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.adri833.orpheus.R
import com.adri833.orpheus.navigation.Routes

data class BottomNavItem(
    @StringRes val label: Int,
    val filledIcon: Painter,
    val outlinedIcon: Painter,
    val route: String
)

object BottomNavItemsProvider {
    @Composable
    fun getBottomNavItems(): List<BottomNavItem> {
        return listOf(
            BottomNavItem(
                label = R.string.home,
                filledIcon = painterResource(id = R.drawable.ic_home_fill),
                outlinedIcon = painterResource(id = R.drawable.ic_home_outlined),
                route = Routes.Home.route
            ),
            BottomNavItem(
                label = R.string.playlist,
                filledIcon = painterResource(id = R.drawable.ic_playlist_fill),
                outlinedIcon = painterResource(id = R.drawable.ic_playlist_outlined),
                route = Routes.Playlist.route
            ),
            BottomNavItem(
                label = R.string.drive,
                filledIcon = painterResource(id = R.drawable.ic_drive_fill),
                outlinedIcon = painterResource(id = R.drawable.ic_drive_outlined),
                route = Routes.Drive.route
            ),
            BottomNavItem(
                label = R.string.youtube,
                filledIcon = painterResource(id = R.drawable.ic_youtube_fill),
                outlinedIcon = painterResource(id = R.drawable.ic_youtube_outlined),
                route = Routes.Downloader.route
            )
        )
    }
}
