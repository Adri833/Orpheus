package com.adri833.orpheus.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun pulseScale(
    pressed: Boolean,
    targetScale: Float = 0.9f,
    onAnimationEnd: () -> Unit = {}
): Float {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(pressed) {
        if (pressed) {
            scale.animateTo(targetScale, animationSpec = tween(100))
            scale.animateTo(1f, animationSpec = tween(100))
            onAnimationEnd()
        }
    }

    return scale.value
}

@Composable
fun slidingOffset(
    key: String,
    forward: Boolean = true,
    distance: Float = 300f,
    durationOut: Int = 100,
    durationIn: Int = 150,
): Float {
    val offsetX = remember { Animatable(0f) }
    var oldKey by remember { mutableStateOf(key) }
    var animating by remember { mutableStateOf(false) }

    LaunchedEffect(key) {
        if (key != oldKey && !animating) {
            animating = true
            if (forward) {
                offsetX.animateTo(
                    targetValue = -distance,
                    animationSpec = tween(durationMillis = durationOut, easing = LinearEasing)
                )
                offsetX.snapTo(distance)
                offsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = durationIn,
                        easing = LinearOutSlowInEasing
                    )
                )
            } else {
                offsetX.animateTo(
                    targetValue = distance,
                    animationSpec = tween(durationMillis = durationOut, easing = LinearEasing)
                )
                offsetX.snapTo(-distance)
                offsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = durationIn,
                        easing = LinearOutSlowInEasing
                    )
                )
            }
            oldKey = key
            animating = false
        }
    }

    return offsetX.value
}

@Composable
fun fadeInAnimation(durationMillis: Int = 150): EnterTransition {
    return fadeIn(animationSpec = tween(durationMillis = durationMillis))
}

@Composable
fun fadeOutAnimation(durationMillis: Int = 150): ExitTransition {
    return fadeOut(animationSpec = tween(durationMillis = durationMillis))
}

@Composable
fun fadeExpandIn(durationMillis: Int = 150): EnterTransition {
    return fadeIn(animationSpec = tween(durationMillis)) +
            expandHorizontally(animationSpec = tween(durationMillis))
}

@Composable
fun fadeShrinkOut(durationMillis: Int = 150): ExitTransition {
    return fadeOut(animationSpec = tween(durationMillis)) +
            shrinkHorizontally(animationSpec = tween(durationMillis))
}

@Composable
fun slideInUpAnimation(
    durationMillis: Int = 150
): EnterTransition = slideInVertically(
    initialOffsetY = { fullHeight -> fullHeight },
    animationSpec = tween(durationMillis)
)

@Composable
fun slideOutUpAnimation(
    durationMillis: Int = 150
): ExitTransition = slideOutVertically(
    targetOffsetY = { fullHeight -> -fullHeight },
    animationSpec = tween(durationMillis)
)

@Composable
fun slideInDownAnimation(
    durationMillis: Int = 150
): EnterTransition = slideInVertically(
    initialOffsetY = { fullHeight -> -fullHeight },
    animationSpec = tween(durationMillis)
)

@Composable
fun slideOutDownAnimation(
    durationMillis: Int = 150
): ExitTransition = slideOutVertically(
    targetOffsetY = { fullHeight -> fullHeight },
    animationSpec = tween(durationMillis)
)