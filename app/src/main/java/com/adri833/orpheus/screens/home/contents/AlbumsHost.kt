package com.adri833.orpheus.screens.home.contents

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.adri833.orpheus.domain.model.Song
import com.adri833.orpheus.screens.home.HomeViewModel
import com.adri833.orpheus.screens.player.PlayerViewModel
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi

@OptIn(UnstableApi::class)
@Composable
fun AlbumsHost(
    allSongs: List<Song>,
    homeViewModel: HomeViewModel,
    playerViewModel: PlayerViewModel
) {
    var selectedAlbum by remember { mutableStateOf<Pair<String, List<Song>>?>(null) }

    BackHandler(enabled = selectedAlbum != null) {
        selectedAlbum = null
    }

    if (selectedAlbum == null) {
        AlbumsContent(
            songs = allSongs,
            onAlbumSelected = { albumName, albumSongs ->
                selectedAlbum = albumName to albumSongs
            }
        )
    } else {
        SongsContent(
            songs = selectedAlbum!!.second,
            homeViewModel = homeViewModel,
            playerViewModel = playerViewModel,
            onBack = { selectedAlbum = null }
        )
    }
}