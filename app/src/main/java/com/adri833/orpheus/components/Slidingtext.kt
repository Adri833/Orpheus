package com.adri833.orpheus.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlidingText(
    text: String,
    color: Color,
    fontWeight: FontWeight,
    fontSize: TextUnit,
    forward: Boolean = true,
) {
    AnimatedContent(
        targetState = text,
        transitionSpec = {
            val animDuration = 300
            if (forward) {
                (slideInHorizontally(
                    animationSpec = tween(animDuration)
                ) { width -> width } + fadeIn(animationSpec = tween(animDuration))).togetherWith(
                    slideOutHorizontally(
                        animationSpec = tween(animDuration)
                    ) { width -> -width } + fadeOut(animationSpec = tween(animDuration))
                )
            } else {
                (slideInHorizontally(
                    animationSpec = tween(animDuration)
                ) { width -> -width } + fadeIn(animationSpec = tween(animDuration))).togetherWith(
                    slideOutHorizontally(
                        animationSpec = tween(animDuration)
                    ) { width -> width } + fadeOut(animationSpec = tween(animDuration))
                )
            }
        }
    ) { targetText ->
        Text(
            text = targetText,
            fontWeight = fontWeight,
            color = color,
            fontSize = fontSize,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}