package com.adri833.orpheus.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp

@Composable
fun NeonGoogleButton(
    text: String,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "colorSweep")
    val offset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

    val borderWidth = 3.dp
    val shape = RoundedCornerShape(50)

    val baseColors = listOf(
        Color(0xFF4285F4),
        Color(0xFFDB4437),
        Color(0xFFF4B400),
        Color(0xFF0F9D58),
        Color(0xFF4285F4),
    )

    val shiftedColors = run {
        val size = baseColors.size
        val shifted = mutableListOf<Color>()

        for (i in 0 until size - 1) {
            val fIndex = (i + offset.value * (size - 1)) % (size - 1)
            val iLow = fIndex.toInt()
            val iHigh = (iLow + 1) % (size - 1)
            val fraction = fIndex - iLow
            shifted.add(lerp(baseColors[iLow], baseColors[iHigh], fraction))
        }

        shifted + shifted.first()
    }

    val brush = Brush.sweepGradient(shiftedColors)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .graphicsLayer(rotationZ = offset.value * 0.01f)
            .border(BorderStroke(borderWidth, brush), shape)
            .padding(borderWidth)
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = shape,
            modifier = Modifier
                .defaultMinSize(minWidth = 180.dp, minHeight = 48.dp)
        ) {
            Text(text = text, color = Color.White)
        }
    }
}