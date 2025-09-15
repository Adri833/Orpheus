package com.adri833.orpheus.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.adri833.orpheus.domain.model.Song

@Composable
fun AlbumCoverWithVinyl(song: Song) {
    Box {
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = canvasHeight / 2f

            val scaleFactor = 0.8f
            val scaledRadius = radius * scaleFactor

            val left = canvasWidth - scaledRadius * 1.5f
            val top = (canvasHeight - (canvasHeight * scaleFactor)) / 2f

            drawArc(
                color = Color.LightGray,
                startAngle = -90f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(left, top),
                size = androidx.compose.ui.geometry.Size(scaledRadius * 2f, canvasHeight * scaleFactor)
            )

            drawArc(
                color = Color.DarkGray,
                startAngle = -90f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(left + scaledRadius * 0.4f, top + scaledRadius * 0.4f),
                size = androidx.compose.ui.geometry.Size(scaledRadius * 1.2f, scaledRadius * 1.2f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
            )

            drawArc(
                color = Color.DarkGray,
                startAngle = -90f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(left + scaledRadius * 0.7f, top + scaledRadius * 0.7f),
                size = androidx.compose.ui.geometry.Size(scaledRadius * 0.6f, scaledRadius * 0.6f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )
        }

        AlbumCover(song.albumArt, size = 76)
    }
}
