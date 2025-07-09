package com.adri833.orpheus.screens.home.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.adri833.orpheus.components.SongItem
import com.adri833.orpheus.screens.home.HomeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.R
import com.adri833.orpheus.components.PlayButton
import com.adri833.orpheus.screens.player.PlayerViewModel

@Composable
fun SongsContent(
    homeViewModel: HomeViewModel,
    playerViewModel: PlayerViewModel
) {
    val songs by homeViewModel.songs.collectAsState(initial = emptyList())
    val currentSong by playerViewModel.currentSong.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState()

    LaunchedEffect(songs) {
        if (songs.isNotEmpty()) {
            playerViewModel.loadSongs(songs)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.shuffle),
                color = Color.Red,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.merriweather_regular)),
            )

            Spacer(modifier = Modifier.width(12.dp))

            PlayButton(
                isPlaying = playerViewModel.isPlaying,
                onPlayClick = { playerViewModel.playOrResume() },
                onPauseClick = { playerViewModel.pause() }
            )
        }

        LazyColumn {
            items(songs) { song ->
                SongItem(
                    song,
                    currentSong,
                    isPlaying
                ) {
                    playerViewModel.onSongSelected(it)
                }
            }
        }
    }
}