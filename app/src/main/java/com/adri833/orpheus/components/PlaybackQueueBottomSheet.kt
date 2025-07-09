package com.adri833.orpheus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.screens.player.PlayerViewModel
import com.adri833.orpheus.R
import com.adri833.orpheus.ui.theme.Gold
import com.adri833.orpheus.utils.ArtistText
import com.adri833.orpheus.utils.NameText
import com.adri833.orpheus.utils.noRippleClickable

@Composable
fun PlaybackQueueContent(
    playerViewModel: PlayerViewModel,
    onDismissRequest: () -> Unit
) {
    val queue by playerViewModel.queue.collectAsState()
    val currentIndex by playerViewModel.currentIndex.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState()

    val visibleQueue = remember(queue, currentIndex) {
        if (currentIndex in queue.indices) {
            queue.subList(currentIndex, queue.size)
        } else {
            emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.queue),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Canción actual (fija arriba)
        if (visibleQueue.isNotEmpty()) {
            val currentSong = visibleQueue.first()
            Surface(
                tonalElevation = 1.dp,
                shadowElevation = 6.dp,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AlbumCover(currentSong)

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            NowPlayingEqualizer(isPlaying = isPlaying)
                            Spacer(modifier = Modifier.width(6.dp))
                            NameText(currentSong.title, color = Gold)
                        }

                        ArtistText(currentSong.artist)
                    }

                    PlayButton(
                        isPlaying = playerViewModel.isPlaying,
                        onPlayClick = { playerViewModel.playOrResume() },
                        onPauseClick = { playerViewModel.pause() },
                        modifier = Modifier.size(40.dp)
                    )
                }
            }


            // Resto de la cola (deslizable)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(visibleQueue.drop(1)) { index, song ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(Color.Transparent)
                            .noRippleClickable {
                                playerViewModel.onSongSelected(queue[currentIndex + index + 1])
                            }
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AlbumCover(song)

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            NameText(song.title, color = Color.White)
                            ArtistText(song.artist)
                        }
                    }
                }
            }
        }
    }
}