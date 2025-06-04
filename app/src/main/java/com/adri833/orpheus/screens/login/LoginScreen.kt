package com.adri833.orpheus.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.components.OrpheusLogo
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.adri833.orpheus.components.NeonGoogleButton
import com.adri833.orpheus.components.WavyText
import kotlinx.coroutines.delay

@Composable
fun LoginScreen() {
    val logoOffsetY = remember { Animatable(0f) }
    var showText by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000L)
        logoOffsetY.animateTo(
            targetValue = -250f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
        delay(300L)
        showText = true
        delay(600L)
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = logoOffsetY.value.dp)
        ) {
            OrpheusLogo(
                modifier = Modifier.size(280.dp)
            )

            AnimatedVisibility(visible = showText) {
                WavyText("Orpheus")
            }
        }

        AnimatedVisibility(
            visible = showButton,
            modifier = Modifier.align(Alignment.Center)
                .offset(y = 100.dp)
        ) {
            NeonGoogleButton(
                text = "Iniciar sesión con Google",
                onClick = {}
            )
        }
    }
}