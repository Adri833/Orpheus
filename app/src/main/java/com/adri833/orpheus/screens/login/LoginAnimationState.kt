package com.adri833.orpheus.screens.login

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.MutableState

data class LoginAnimationState(
    val logoOffsetY: Animatable<Float, *>,
    val showText: MutableState<Boolean>,
    val showText2: MutableState<Boolean>,
    val showButton: MutableState<Boolean>,
    val buttonExitScale: Animatable<Float, *>,
    val contentAlpha: Animatable<Float, *>,
    val performExitAnimation: MutableState<Boolean>,
    val buttonOffsetY: Animatable<Float, *>
)
