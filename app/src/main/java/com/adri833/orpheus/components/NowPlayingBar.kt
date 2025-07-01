package com.adri833.orpheus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.screens.player.PlayerViewModel

@Composable
fun NowPlayingBar(
    viewModel: PlayerViewModel,
) {
    val currentSong by viewModel.currentSong.collectAsState()

    if (currentSong != null) {
        val song = currentSong!!
        val isPlaying = remember { mutableStateOf(viewModel.isPlaying()) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color.DarkGray)
                .padding(horizontal = 16.dp)
                .clickable { /* TODO: Navegar a pantalla de reproducción completa */ },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = song.title,
                    color = Color.White
                )
                Text(
                    text = song.artist,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Icon(
                imageVector = if (isPlaying.value) Icons.Default.Close else Icons.Default.PlayArrow,
                contentDescription = "Play/Pause",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        if (isPlaying.value) {
                            viewModel.pause()
                        } else {
                            viewModel.resume()
                        }
                        isPlaying.value = !isPlaying.value
                    }
            )
        }
    }
}