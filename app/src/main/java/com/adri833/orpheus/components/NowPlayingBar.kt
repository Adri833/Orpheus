package com.adri833.orpheus.components

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.adri833.orpheus.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.adri833.orpheus.screens.player.PlayerViewModel
import com.adri833.orpheus.ui.theme.rememberSongColors
import com.adri833.orpheus.utils.lighten
import com.adri833.orpheus.utils.mixWithWhite
import com.adri833.orpheus.utils.noRippleClickable

@OptIn(UnstableApi::class)
@Composable
fun NowPlayingBar(
    viewModel: PlayerViewModel,
    onQueueClick: () -> Unit,
    navigationToSong: () -> Unit
) {
    val context = LocalContext.current
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val progress by viewModel.playbackProgress.collectAsState()
    val song = currentSong ?: return
    val songColors = rememberSongColors(context, song.albumArt ?: song.contentUri)
    val contentColor = songColors.contentColor

    var totalDragX by remember { mutableFloatStateOf(0f) }
    val forward by viewModel.isForward.collectAsState()

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        songColors.backgroundColor.mixWithWhite(0.6f),
                        songColors.backgroundColor.lighten(0.6f),
                    )
                )
            )
            .padding(end = 12.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (totalDragX < -100f) viewModel.skipToNext()
                        else if (totalDragX > 100f) viewModel.skipToPrevious()
                        totalDragX = 0f
                    },
                    onDragCancel = { totalDragX = 0f },
                    onDrag = { change, dragAmount ->
                        totalDragX += dragAmount.x
                        change.consume()
                    }
                )
            }
            .noRippleClickable { navigationToSong() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AlbumCover(song, 58)

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f).clipToBounds()
        ) {
            SlidingText(
                text = song.title,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                forward = forward
            )

            SlidingText(
                text = song.artist,
                color = contentColor,
                fontWeight = FontWeight.Normal,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                forward = forward
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        ProgressIconButton(
            isPlaying = isPlaying,
            progress = progress,
            backgroundColor = songColors.backgroundColor,
            contentColor = contentColor,
            onClick = {
                if (isPlaying) viewModel.pause() else viewModel.togglePlayback()
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painter = painterResource(R.drawable.ic_queue),
            contentDescription = "Queue",
            tint = contentColor,
            modifier = Modifier.size(30.dp).noRippleClickable { onQueueClick() }
        )
    }
}