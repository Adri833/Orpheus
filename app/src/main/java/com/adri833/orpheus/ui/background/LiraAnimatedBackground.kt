package com.adri833.orpheus.ui.background

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.ui.theme.Gold
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@Composable
fun LiraAnimatedBackground(modifier: Modifier = Modifier) {
    val lineColor = Gold
    val numStrings = 5
    val segmentCount = 100
    val baseWaveAmplitude = 8f
    val waveLength = 80f
    val waveSpeed = 0.5f

    val time = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            time.animateTo(
                targetValue = time.value + 1f,
                animationSpec = tween(durationMillis = 16, easing = LinearEasing)
            )
        }
    }

    val vibrationAmplitudes = remember {
        List(numStrings) { Animatable(0f) }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val indices = (0 until numStrings).shuffled().toMutableList()
            for (index in indices) {
                // Pulsa cuerda
                vibrationAmplitudes[index].snapTo(1f)
                val anim = launch {
                    vibrationAmplitudes[index].animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
                    )
                }

                while (vibrationAmplitudes[index].value > 0.2f) {
                    delay(50)
                }

                anim.join()
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val spacing = 26.dp.toPx()
        val marginBottom = 130.dp.toPx()
        val centerY = size.height - marginBottom
        val stringThickness = 2.dp.toPx()
        val width = size.width
        val step = width / segmentCount

        for (i in 0 until numStrings) {
            val y = centerY + (i - (numStrings - 1) / 2f) * spacing
            val intensity = vibrationAmplitudes[i].value
            val path = Path()

            for (j in 0..segmentCount) {
                val x = j * step
                val waveOffset = sin((x / waveLength) + (time.value * waveSpeed)) * baseWaveAmplitude * intensity
                val point = Offset(x, y + waveOffset)
                if (j == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = stringThickness)
            )
        }
    }
}