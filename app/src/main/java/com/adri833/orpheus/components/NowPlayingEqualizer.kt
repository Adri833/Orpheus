package com.adri833.orpheus.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.isActive

@Composable
fun NowPlayingEqualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.equalizer))
    val progress = remember { Animatable(0f) }

    LaunchedEffect(composition, isPlaying) {
        if (composition == null) return@LaunchedEffect

        if (isPlaying) {
            while (isActive) {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = composition!!.duration.toInt(),
                        easing = LinearEasing
                    )
                )
                progress.snapTo(0f)
            }
        } else {
            progress.stop()
        }
    }

    LottieAnimation(
        composition = composition,
        progress = { progress.value },
        modifier = modifier.size(24.dp)
    )
}