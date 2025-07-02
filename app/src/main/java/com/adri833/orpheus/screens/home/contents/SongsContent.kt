package com.adri833.orpheus.screens.home.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.R
import com.adri833.orpheus.screens.player.PlayerViewModel

@Composable
fun SongsContent(
    viewModel: HomeViewModel,
    playerViewModel: PlayerViewModel
) {
    val songs by viewModel.songs.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadSongs()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.shuffle),
                color = Color.Red,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.merriweather_regular)),
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(songs, key = { it.id }) { song ->
                SongItem(song, onClick = { clickedSong ->
                    playerViewModel.playSong(clickedSong)
                })
            }
        }
    }
}