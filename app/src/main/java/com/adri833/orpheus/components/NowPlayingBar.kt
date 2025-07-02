package com.adri833.orpheus.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.adri833.orpheus.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.screens.player.PlayerViewModel
import com.adri833.orpheus.ui.theme.Gray
import com.adri833.orpheus.ui.theme.MediumGray
import com.adri833.orpheus.utils.ArtistText
import com.adri833.orpheus.utils.NameText
import com.adri833.orpheus.utils.getDominantColor
import com.adri833.orpheus.utils.isColorDark
import com.adri833.orpheus.utils.lighten
import com.adri833.orpheus.utils.mixWithWhite
import com.adri833.orpheus.utils.noRippleClickable

@Composable
fun NowPlayingBar(
    viewModel: PlayerViewModel,
) {
    val context = LocalContext.current
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val progress by viewModel.playbackProgress.collectAsState()
    val song = currentSong ?: return
    var dominantColor by remember { mutableStateOf(Color.LightGray) }
    val contentColor = if (isColorDark(dominantColor)) Color.White else Color.Black
    val borderColor = if (contentColor == Color.White) Gray else MediumGray

    LaunchedEffect(song.contentUri) {
        dominantColor = getDominantColor(context, song.albumArt ?: song.contentUri)
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
            .clickable { /* TODO: Navegar a pantalla de reproducción completa */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AlbumCover(song, 64)

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            NameText(name = song.title, color = contentColor)

            ArtistText(artist = song.artist, color = contentColor)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier.size(36.dp),
            contentAlignment = Alignment.Center
        ) {
            // Círculo de progreso (Stroke)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 3.dp.toPx()
                val diameter = size.minDimension - strokeWidth
                val topLeft = Offset(
                    (size.width - diameter) / 2,
                    (size.height - diameter) / 2
                )

                // Borde completo en gris claro
                drawArc(
                    color = borderColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    topLeft = topLeft,
                    size = Size(diameter, diameter)
                )

                // Línea de progreso color contraste
                drawArc(
                    color = contentColor,
                    startAngle = -90f,
                    sweepAngle = 360 * progress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    topLeft = topLeft,
                    size = Size(diameter, diameter)
                )
            }


            Icon(
                painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = "Play/Pause",
                tint = contentColor,
                modifier = Modifier
                    .size(22.dp)
                    .noRippleClickable(
                        onClick = {
                            if (isPlaying) {
                                viewModel.pause()
                            } else {
                                viewModel.resume()
                            }
                        }
                    )

            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painter = painterResource(R.drawable.ic_queue),
            contentDescription = "Queue",
            tint = contentColor,
            modifier = Modifier
                .size(30.dp)
                .noRippleClickable(
                    onClick = { /* TODO: Navegar a pantalla de cola de reproducción */ }
                )
        )
    }
}