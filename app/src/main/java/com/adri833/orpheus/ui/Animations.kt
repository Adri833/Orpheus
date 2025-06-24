package com.adri833.orpheus.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable

fun pulseScaleSlim(pressed: Boolean, onAnimationEnd: () -> Unit): Float {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(pressed) {
        if (pressed) {
            scale.animateTo(0.9f, animationSpec = tween(100))
            scale.animateTo(1f, animationSpec = tween(100))
            onAnimationEnd()
        }
    }

    return scale.value
}

@Composable
fun pulseScale(pressed: Boolean, onAnimationEnd: () -> Unit): Float {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(pressed) {
        if (pressed) {
            scale.animateTo(0.8f, animationSpec = tween(100))
            scale.animateTo(1f, animationSpec = tween(100))
            onAnimationEnd()
        }
    }

    return scale.value
}