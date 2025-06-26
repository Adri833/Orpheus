package com.adri833.orpheus.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween

// Durations
const val DURATION_SHORT = 300
const val DURATION_NORMAL = 600
const val DURATION_LONG = 1000
const val DURATION_EXTRA = 1500

// Targets
const val ALPHA_VISIBLE = 1f
const val ALPHA_HIDDEN = 0f

// Transitions
val transitionFadeShort: TweenSpec<Float>
    get() = tween(durationMillis = DURATION_SHORT, easing = FastOutSlowInEasing)

val transitionFadeNormal: TweenSpec<Float>
    get() = tween(durationMillis = DURATION_NORMAL, easing = FastOutSlowInEasing)

val transitionFadeLong: TweenSpec<Float>
    get() = tween(durationMillis = DURATION_LONG, easing = FastOutSlowInEasing)

val transitionFadeExtra: TweenSpec<Float>
    get() = tween(durationMillis = DURATION_EXTRA, easing = FastOutSlowInEasing)

