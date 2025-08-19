package com.adri833.orpheus.screens.song

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.adri833.orpheus.R
import com.adri833.orpheus.components.AlbumCover
import com.adri833.orpheus.components.PlayButton
import com.adri833.orpheus.components.PlaybackQueueBottomSheet
import com.adri833.orpheus.components.ShuffleButton
import com.adri833.orpheus.screens.player.PlayerViewModel
import com.adri833.orpheus.ui.theme.Gold
import com.adri833.orpheus.ui.theme.rememberSongColors
import com.adri833.orpheus.utils.ArtistText
import com.adri833.orpheus.utils.NameText
import com.adri833.orpheus.utils.noRippleClickable
import kotlinx.coroutines.delay

@ExperimentalMaterial3Api
@OptIn(UnstableApi::class)
@Composable
fun SongScreen(
    playerViewModel: PlayerViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val song by playerViewModel.currentSong.collectAsState()
    val progress by playerViewModel.playbackProgress.collectAsState()
    var visible by remember { mutableStateOf(false) }
    var isBackHandled by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showQueue by remember { mutableStateOf(false) }
    val songColors = rememberSongColors(context, song?.albumArt ?: song?.contentUri)
    val contentColor = songColors.contentColor

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(songColors.backgroundColor)
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
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                AlbumCover(song = current, size = 290)

                Spacer(modifier = Modifier.height(26.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    NameText(current.title, color = contentColor, fontSize = 22f)

                    Spacer(modifier = Modifier.height(8.dp))

                    ArtistText(current.artist, color = contentColor.copy(alpha = 0.6f), fontSize = 16f)
                }

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Tiempo actual a la izquierda
                    Text(
                        text = formatTime((progress * (playerViewModel.playerManager.player.duration)).toLong()),
                        color = contentColor.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    // Barra de progreso
                    Slider(
                        value = progress,
                        onValueChange = { newValue ->
                            val duration = playerViewModel.playerManager.player.duration
                            val seekTo = (newValue * duration).toLong()
                            playerViewModel.playerManager.player.seekTo(seekTo)
                        },
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = contentColor,
                            activeTrackColor = contentColor,
                            inactiveTrackColor = contentColor.copy(alpha = 0.6f)
                        ),
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    // Tiempo total a la derecha
                    Text(
                        text = formatTime(playerViewModel.playerManager.player.duration),
                        color = contentColor.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(38.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShuffleButton(playerViewModel, size = 36, colorEnabled = Gold.copy(alpha = 0.9f) ,colorDisabled = contentColor.copy(alpha = 0.6f))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_previous),
                        contentDescription = "Anterior",
                        tint = contentColor,
                        modifier = Modifier
                            .size(36.dp)
                            .noRippleClickable(onClick = { playerViewModel.skipToPrevious() })
                    )

                    PlayButton(
                        isPlaying = playerViewModel.isPlaying,
                        onPlayClick = { playerViewModel.togglePlayback() },
                        onPauseClick = { playerViewModel.togglePlayback() },
                        backgroundColor = contentColor,
                        iconColor = songColors.backgroundColor
                    )


                    Icon(
                        painter = painterResource(id = R.drawable.ic_next),
                        contentDescription = "Siguiente",
                        tint = contentColor,
                        modifier = Modifier
                            .size(36.dp)
                            .noRippleClickable(onClick = { playerViewModel.skipToNext() })
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_queue),
                        contentDescription = "Queue",
                        tint = contentColor.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(36.dp)
                            .noRippleClickable { showQueue = true }
                    )
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

    if (showQueue) {
        androidx.compose.material3.ModalBottomSheet(
            onDismissRequest = { showQueue = false },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        ) {
            PlaybackQueueBottomSheet(
                playerViewModel = playerViewModel,
                onDismissRequest = { showQueue = false },
            )
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val totalSeconds = (milliseconds / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}