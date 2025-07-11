package com.adri833.orpheus.screens.song

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.R
import com.adri833.orpheus.components.AlbumCover
import com.adri833.orpheus.components.ShuffleButton
import com.adri833.orpheus.screens.player.PlayerViewModel
import com.adri833.orpheus.utils.ArtistText
import com.adri833.orpheus.utils.NameText
import kotlinx.coroutines.delay


@Composable
fun SongScreen(
    viewModel: PlayerViewModel,
    onBack: () -> Unit
) {
    val song by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val progress by viewModel.playbackProgress.collectAsState()
    var visible by remember { mutableStateOf(false) }
    var isBackHandled by remember { mutableStateOf(false) }

    BackHandler {
        if (!isBackHandled) {
            isBackHandled = true
            visible = false
        }
    }

    LaunchedEffect(Unit) {
        visible = true
    }

    BackHandler(enabled = visible) {
        visible = false
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        song?.let { current ->
            // TODO: Colors from cover and text
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                IconButton(
                    onClick = {
                        if (!isBackHandled) {
                            isBackHandled = true
                            visible = false
                        }
                    },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = "Volver",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                AlbumCover(song = current, size = 330)

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    NameText(current.title, color = Color.White, fontSize = 22f)

                    Spacer(modifier = Modifier.height(8.dp))

                    ArtistText(current.artist, color = Color.Gray, fontSize = 16f)
                }

                Spacer(modifier = Modifier.height(50.dp))

                Slider(
                    value = progress,
                    onValueChange = { newValue ->
                        val duration = viewModel.playerManager.player.duration
                        val seekTo = (newValue * duration).toLong()
                        viewModel.playerManager.player.seekTo(seekTo)
                    },
                    modifier = Modifier,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.Gray
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Tiempo actual a la izquierda
                    Text(
                        text = formatTime((progress * (viewModel.playerManager.player.duration)).toLong()),
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    // Tiempo total a la derecha
                    Text(
                        text = formatTime(viewModel.playerManager.player.duration),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(38.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShuffleButton(viewModel)

                    IconButton(onClick = { viewModel.skipToPrevious() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_previous),
                            contentDescription = "Anterior",
                            tint = Color.White,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    IconButton(onClick = { viewModel.playOrResume() }) {
                        Icon(
                            painter = if (isPlaying)
                                painterResource(id = R.drawable.ic_pause) else
                                painterResource(id = R.drawable.ic_play),
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    IconButton(onClick = { viewModel.skipToNext() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_next), // drawable pendiente
                            contentDescription = "Siguiente",
                            tint = Color.White,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    ShuffleButton(viewModel)
                }
            }

        }
    }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(250)
            onBack()
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val totalSeconds = (milliseconds / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}