package com.adri833.orpheus.screens.home.contents

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.adri833.orpheus.components.SongItem
import com.adri833.orpheus.screens.home.HomeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import com.adri833.orpheus.domain.handler.RequestAudioPermission

@Composable
fun SongsContent(
    viewModel: HomeViewModel,
) {
    val songs by viewModel.songs.collectAsState(initial = emptyList())

    RequestAudioPermission {
        viewModel.loadSongs()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(songs) { song ->
            SongItem(song, onClick = { clickedSong ->
                viewModel.playSong(clickedSong)
            })
        }
    }
}