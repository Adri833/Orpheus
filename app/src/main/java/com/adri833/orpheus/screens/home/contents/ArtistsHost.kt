package com.adri833.orpheus.screens.home.contents

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.util.UnstableApi
import com.adri833.orpheus.domain.model.Song
import com.adri833.orpheus.screens.home.HomeViewModel
import com.adri833.orpheus.screens.player.PlayerViewModel

@OptIn(UnstableApi::class)
@Composable
fun ArtistsHost(
    allSongs: List<Song>,
    homeViewModel: HomeViewModel,
    playerViewModel: PlayerViewModel
) {
    var selectedArtist by remember { mutableStateOf<Pair<String, List<Song>>?>(null) }

    BackHandler(enabled = selectedArtist != null) {
        selectedArtist = null
    }

    if (selectedArtist == null) {
        ArtistsContent(
            songs = allSongs,
            onArtistSelected = { artistName, artistSongs ->
                selectedArtist = artistName to artistSongs
            }
        )
    } else {
        SongsContent(
            songs = selectedArtist!!.second,
            homeViewModel = homeViewModel,
            playerViewModel = playerViewModel,
            onBack = { selectedArtist = null },
            navigationToInformation = { }
        )
    }
}