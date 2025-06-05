package com.adri833.orpheus.ui.background

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.adri833.orpheus.ui.theme.Gold
import kotlin.math.*

@Composable
fun PentagramAnimation() {
    val numLines = 5
    val amplitudes = remember { List(numLines) { mutableFloatStateOf(0f) } }
    val phases = remember { List(numLines) { it * PI / 6 } }
    val vibrationStartTimes = remember { LongArray(numLines) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()

        repeat(numLines) { i ->
            vibrationStartTimes[i] = startTime + i * 100_000_000L
        }

        while (true) {
            val currentTime = System.nanoTime()
            repeat(numLines) { i ->
                val t = (currentTime - vibrationStartTimes[i]) / 1_000_000_000f
                if (t >= 0f) {
                    val damping = exp(-2 * t)
                    val wave = sin(15 * t) * damping
                    amplitudes[i].floatValue = wave * 12f
                }
            }
            withFrameNanos {}
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension / 3f
        val spacing = 20f

        repeat(numLines) { i ->
            val path = Path()
            val offset = (i - 2) * spacing
            for (angle in 0..360 step 2) {
                val radians = Math.toRadians(angle.toDouble())
                val wave = sin(radians * 6 + phases[i]) * amplitudes[i].floatValue
                val r = baseRadius + offset + wave
                val x = center.x + cos(radians) * r
                val y = center.y + sin(radians) * r

                if (angle == 0) {
                    path.moveTo(x.toFloat(), y.toFloat())
                } else {
                    path.lineTo(x.toFloat(), y.toFloat())
                }
            }
            path.close()

            drawPath(
                path = path,
                color = Gold,
                style = Stroke(width = 2f)
            )
        }
    }
}