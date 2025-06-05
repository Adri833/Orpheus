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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.adri833.orpheus.R
import com.adri833.orpheus.components.NeonGoogleButton
import com.adri833.orpheus.components.WavyText
import com.adri833.orpheus.components.adjustForMobile
import com.adri833.orpheus.ui.background.LiraAnimatedBackground
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val logoOffsetY = remember { Animatable(0f) }
    var showText by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }
    var showLira by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000L)
        logoOffsetY.animateTo(
            targetValue = -180f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
        delay(300L)
        showText = true
        delay(600L)
        showButton = true
        delay(800L)
        showLira = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .adjustForMobile(),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = showLira,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        ) {
            LiraAnimatedBackground()
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = logoOffsetY.value.dp)
        ) {
            OrpheusLogo(
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(visible = showText) {
                WavyText(stringResource(R.string.app_name))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn(animationSpec = tween(durationMillis = 800)),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 60.dp)
        ) {
            NeonGoogleButton(
                onClick = {viewModel.loginWithGoogle(context, scope)}
            )
        }
    }
}