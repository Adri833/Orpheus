package com.adri833.orpheus.screens.home.contents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import com.adri833.orpheus.components.SongItem
import com.adri833.orpheus.screens.home.HomeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.adri833.orpheus.components.PlayButton
import com.adri833.orpheus.components.ShuffleButton
import com.adri833.orpheus.components.SongSearchBar
import com.adri833.orpheus.domain.model.Song
import com.adri833.orpheus.screens.player.PlayerViewModel

@UnstableApi
@Composable
fun SongsContent(
    songs: List<Song>,
    homeViewModel: HomeViewModel,
    playerViewModel: PlayerViewModel,
    onBack: (() -> Unit)? = null
) {
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val currentSong by playerViewModel.currentSong.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState()

    LaunchedEffect(songs) {
        if (songs.isNotEmpty()) {
            playerViewModel.loadSongs(songs)
        }
    }

    val filteredSongs = songs.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
        it.artist.contains(searchQuery, ignoreCase = true) ||
        it.album.contains(searchQuery, ignoreCase = true)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        onBack?.let {
            IconButton(onClick = it) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Favorite Icon",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
            }
        }

        ShuffleButton(playerViewModel)

        Spacer(modifier = Modifier.width(12.dp))

        PlayButton(
            isPlaying = playerViewModel.isPlaying,
            onPlayClick = { playerViewModel.playOrResume(filteredSongs) },
            onPauseClick = { playerViewModel.pause() }
        )

        Spacer(modifier = Modifier.weight(1f))

        SongSearchBar(
            query = searchQuery,
            onQueryChange = { homeViewModel.updateSearchQuery(it) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }

    LazyColumn {
        items(filteredSongs) { song ->
            SongItem(
                song = song,
                currentSong = currentSong,
                isPlaying = isPlaying
            ) {
                playerViewModel.onSongSelected(it)
            }
        }
    }
}