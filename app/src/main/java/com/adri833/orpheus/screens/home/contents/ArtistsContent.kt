package com.adri833.orpheus.screens.home.contents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.adri833.orpheus.components.ArtistItem
import com.adri833.orpheus.domain.model.Song

@Composable
fun ArtistsContent(
    songs: List<Song>,
    onArtistSelected: (artistName: String, artistSongs: List<Song>) -> Unit
) {
    val artists = remember(songs) {
        songs.groupBy { it.artist }.map { it.key to it.value }
    }

    LazyColumn {
        items(artists) { (artistName, artistSongs) ->
            ArtistItem(
                artistName = artistName,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onArtistSelected(artistName, artistSongs) }
            )
        }
    }
}