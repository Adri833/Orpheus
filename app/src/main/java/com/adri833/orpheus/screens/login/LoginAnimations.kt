package com.adri833.orpheus.screens.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.adri833.orpheus.util.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberLoginAnimationState(loginState: UiState<Unit>): LoginAnimationState {
    val logoOffsetY = remember { Animatable(0f) }
    val showText = remember { mutableStateOf(false) }
    val showButton = remember { mutableStateOf(false) }
    val showLira = remember { mutableStateOf(false) }

    val buttonExitScale = remember { Animatable(1f) }
    val contentAlpha = remember { Animatable(1f) }
    val performExitAnimation = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000L)
        logoOffsetY.animateTo(-180f, tween(600, easing = FastOutSlowInEasing))
        delay(300)
        showText.value = true
        delay(600)
        showButton.value = true
        delay(800)
        showLira.value = true
    }

    LaunchedEffect(loginState) {
        if (loginState is UiState.Success) {
            performExitAnimation.value = true
            showButton.value = false
            delay(1000)

            launch {
                buttonExitScale.animateTo(20f, tween(1200, easing = EaseIn))
            }

            launch {
                contentAlpha.animateTo(0f, tween(400, delayMillis = 200))
            }

            delay(1200)
        }
    }

    return LoginAnimationState(
        logoOffsetY, showText, showButton, showLira,
        buttonExitScale, contentAlpha, performExitAnimation
    )
}
