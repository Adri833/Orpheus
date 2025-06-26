package com.adri833.orpheus.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adri833.orpheus.components.OrpheusLogo
import com.adri833.orpheus.screens.login.LoginViewModel
import com.adri833.orpheus.ui.ALPHA_HIDDEN
import com.adri833.orpheus.ui.ALPHA_VISIBLE
import com.adri833.orpheus.ui.background.PentagramAnimation
import com.adri833.orpheus.ui.transitionFadeLong
import com.adri833.orpheus.ui.transitionFadeNormal
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
            targetValue = ALPHA_VISIBLE,
            animationSpec = transitionFadeNormal
        )

        delay(750)

        alphaAnim.animateTo(
            targetValue = ALPHA_HIDDEN,
            animationSpec = transitionFadeNormal
        )

        if (viewModel.isUserLoggedIn()) {
            navigationToHome()
        } else {
            navigationToLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .alpha(alphaAnim.value),
        contentAlignment = Alignment.Center
    ) {

        PentagramAnimation()

        OrpheusLogo(
            modifier = Modifier
                .size(180.dp)
        )
    }
}