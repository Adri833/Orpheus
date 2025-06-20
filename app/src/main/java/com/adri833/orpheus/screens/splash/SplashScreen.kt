package com.adri833.orpheus.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adri833.orpheus.components.OrpheusLogo
import com.adri833.orpheus.screens.login.LoginViewModel
import com.adri833.orpheus.ui.background.PentagramAnimation
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigationToLogin: () -> Unit,
    navigationToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(true) {
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            )
        )
        delay(750)
        if (viewModel.isUserLoggedIn()) {
            navigationToHome()
        } else {
            navigationToLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        PentagramAnimation()

        OrpheusLogo(
            modifier = Modifier
                .size(180.dp)
        )
    }
}