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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.adri833.orpheus.R
import com.adri833.orpheus.components.NeonGoogleButton
import com.adri833.orpheus.components.WavyText
import com.adri833.orpheus.utils.UiState
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigationToHome: () -> Unit
) {
    val context = LocalContext.current
    val loginState by viewModel.loginState
    val animationState = rememberLoginAnimationState(loginState)
    var isButtonClicked by remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        when (loginState) {
            is UiState.Error -> {
                isButtonClicked = false
                val message = (loginState as UiState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }

            UiState.Success -> {
                delay(1800)
                navigationToHome()
            }

            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .graphicsLayer(alpha = animationState.contentAlpha.value),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = !animationState.performExitAnimation.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 0)),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000, delayMillis = 0))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
                    .offset(y = animationState.logoOffsetY.value.dp)
            ) {
                OrpheusLogo(
                    modifier = Modifier.size(180.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                AnimatedVisibility(animationState.showText.value) {
                    WavyText(stringResource(R.string.app_name), delayPerChar = 100L ,fontSize = 54)
                }

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(animationState.showText2.value) {
                    WavyText(stringResource(R.string.slogan), delayPerChar = 30L ,fontSize = 20)
                }
            }
        }

        AnimatedVisibility(
            visible = animationState.showButton.value || animationState.performExitAnimation.value,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = animationState.buttonOffsetY.value.dp)
                .graphicsLayer(
                    scaleX = animationState.buttonExitScale.value,
                    scaleY = animationState.buttonExitScale.value
                )
        ) {
            NeonGoogleButton(
                isLoading = loginState is UiState.Loading && !animationState.performExitAnimation.value,
                enabled = !isButtonClicked && loginState !is UiState.Loading && !animationState.performExitAnimation.value,
                isSuccessAnimationActive = animationState.performExitAnimation.value,
                onClick = {
                    isButtonClicked = true
                    viewModel.loginWithGoogle(context)
                }
            )
        }
    }
}