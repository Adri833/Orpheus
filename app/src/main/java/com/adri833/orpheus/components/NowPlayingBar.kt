package com.adri833.orpheus.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.screens.player.PlayerViewModel
import com.adri833.orpheus.utils.getDominantColor
import com.adri833.orpheus.utils.isColorDark
import com.adri833.orpheus.utils.lighten
import com.adri833.orpheus.utils.mixWithWhite
import com.adri833.orpheus.utils.noRippleClickable

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

    var targetColor by remember { mutableStateOf(Color.LightGray) }
    val dominantColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 200)
    )

    val contentColor = if (isColorDark(dominantColor)) Color.White else Color.Black
    var totalDragX by remember { mutableFloatStateOf(0f) }
    val forward by viewModel.isForward.collectAsState()

    LaunchedEffect(song.contentUri) {
        targetColor = getDominantColor(context, song.albumArt ?: song.contentUri)
    }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        dominantColor.mixWithWhite(0.6f),
                        dominantColor.lighten(0.6f),
                    )
                )
            )
            .padding(end = 12.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (totalDragX < -100f) {
                            viewModel.skipToNext()
                        } else if (totalDragX > 100f) {
                            viewModel.skipToPrevious()
                        }
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
            modifier = Modifier
                .weight(1f)
                .clipToBounds()
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
            backgroundColor = dominantColor,
            contentColor = contentColor,
            onClick = {
                if (isPlaying) viewModel.pause() else viewModel.playOrResume()
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painter = painterResource(R.drawable.ic_queue),
            contentDescription = "Queue",
            tint = contentColor,
            modifier = Modifier
                .size(30.dp)
                .noRippleClickable { onQueueClick() }
        )
    }
}