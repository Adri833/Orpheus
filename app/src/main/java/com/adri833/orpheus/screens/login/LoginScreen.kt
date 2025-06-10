package com.adri833.orpheus.screens.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.components.OrpheusLogo
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.adri833.orpheus.R
import com.adri833.orpheus.components.NeonGoogleButton
import com.adri833.orpheus.components.WavyText
import com.adri833.orpheus.components.adjustForMobile
import com.adri833.orpheus.ui.background.LiraAnimatedBackground
import com.adri833.orpheus.util.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigationToHome: () -> Unit
) {
    val context = LocalContext.current
    val logoOffsetY = remember { Animatable(0f) }
    var showText by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }
    var showLira by remember { mutableStateOf(false) }
    val loginState by viewModel.loginState

    // Estados de salida del boton
    val buttonExitScale = remember { Animatable(1f) }
    val contentAlpha = remember { Animatable(1f) }
    var performExitAnimation by remember { mutableStateOf(false) }

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

    LaunchedEffect(loginState) {
        when (loginState) {
            is UiState.Error -> {
                val message = (loginState as UiState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            UiState.Success -> {
                performExitAnimation = true
                showButton = false

                delay(1000)

                launch {
                    buttonExitScale.animateTo(
                        targetValue = 20f,
                        animationSpec = tween(durationMillis = 1200, easing = EaseIn)
                    )
                }

                launch {
                    contentAlpha.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 400, delayMillis = 200)
                    )
                }

                delay(1200)
                navigationToHome()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .adjustForMobile()
            .graphicsLayer(alpha = contentAlpha.value),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = showLira && !performExitAnimation,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000, delayMillis = 0))
        ) {
            LiraAnimatedBackground()
        }

        AnimatedVisibility(
            visible = !performExitAnimation,
            enter = fadeIn(animationSpec = tween(durationMillis = 0)),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000, delayMillis = 0))
        ) {
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
        }

        AnimatedVisibility(
            visible = showButton || performExitAnimation,
            enter = fadeIn(animationSpec = tween(durationMillis = 800)),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 60.dp)
                .graphicsLayer(
                    scaleX = buttonExitScale.value,
                    scaleY = buttonExitScale.value
                )
        ) {
            NeonGoogleButton(
                isLoading = loginState is UiState.Loading && !performExitAnimation,
                enabled = loginState !is UiState.Loading && !performExitAnimation,
                isSuccessAnimationActive = performExitAnimation,
                onClick = {
                    // Solo permite el click si no estamos en la animación de salida
                    if (!performExitAnimation) {
                        viewModel.loginWithGoogle(context)
                    }
                }
            )
        }
    }
}