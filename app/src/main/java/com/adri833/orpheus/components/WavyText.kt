package com.adri833.orpheus.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.ui.theme.Gold
import com.adri833.orpheus.ui.theme.PlayFairDisplay_SemiBold
import kotlinx.coroutines.delay

@Composable
fun WavyText(
    text: String,
    fontSize: Int,
    delayPerChar: Long
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Gold.copy(alpha = 0.8f),
            Gold,
            Gold.copy(alpha = 0.8f)
        ),
        start = Offset(0f, 0f),
        end = Offset(100f, 100f)
    )

    Row {
        text.forEachIndexed { index, char ->
            val offsetY = remember { Animatable(0f) }

            LaunchedEffect(index) {
                delay(index * delayPerChar)
                offsetY.animateTo(
                    targetValue = -10f,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            Text(
                text = char.toString(),
                fontSize = fontSize.sp,
                fontFamily = PlayFairDisplay_SemiBold,
                modifier = Modifier.offset(y = offsetY.value.dp),
                style = TextStyle(
                    brush = gradientBrush,
                    shadow = Shadow(
                        color = Gold.copy(alpha = 0.6f),
                        offset = Offset(0f, 0f),
                        blurRadius = 12f
                    ),
                    letterSpacing = 1.sp,
                )
            )
        }
    }
}