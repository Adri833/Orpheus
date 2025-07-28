package com.adri833.orpheus.screens.home.contents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.adri833.orpheus.components.AlbumItem
import com.adri833.orpheus.domain.model.Song
import androidx.compose.foundation.lazy.items

@Composable
fun AlbumsContent(
    songs: List<Song>,
    onAlbumSelected: (albumName: String, albumSongs: List<Song>) -> Unit
) {
    val albums = remember(songs) {
        songs.groupBy { it.album }.map { it.key to it.value }
    }

    LazyColumn {
        items(albums) { (albumName, albumSongs) ->
            AlbumItem(
                albumName = albumName,
                song = albumSongs.first(),
                modifier = Modifier.fillMaxWidth(),
                onClick = { onAlbumSelected(albumName, albumSongs) }
            )
        }
    }
}
