package com.adri833.orpheus.screens.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.adri833.orpheus.utils.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberLoginAnimationState(loginState: UiState<Unit>): LoginAnimationState {
    val logoOffsetY = remember { Animatable(0f) }
    val showText = remember { mutableStateOf(false) }
    val showText2 = remember { mutableStateOf(false) }
    val showButton = remember { mutableStateOf(false) }
    val buttonOffsetY = remember { Animatable(160f) }
    val buttonExitScale = remember { Animatable(1f) }
    val contentAlpha = remember { Animatable(1f) }
    val performExitAnimation = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1200)
        logoOffsetY.animateTo(-110f, tween(600, easing = FastOutSlowInEasing))
        delay(300)
        showText.value = true
        delay(1200)
        showText2.value = true
        showButton.value = true
    }

    LaunchedEffect(loginState) {
        if (loginState is UiState.Success) {
            performExitAnimation.value = true
            showButton.value = false

            launch {
                buttonOffsetY.animateTo(
                    targetValue = 60f,
                    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
                )
                delay(300)
                val scaleJob = launch {
                    buttonExitScale.animateTo(5f, tween(800, easing = EaseIn))
                }
                val alphaJob = launch {
                    contentAlpha.animateTo(0f, tween(800))
                }
                scaleJob.join()
                alphaJob.join()
            }
            delay(1000)
        }
    }

    return LoginAnimationState(
        logoOffsetY, showText, showText2, showButton,
        buttonExitScale, contentAlpha, performExitAnimation, buttonOffsetY,
    )
}