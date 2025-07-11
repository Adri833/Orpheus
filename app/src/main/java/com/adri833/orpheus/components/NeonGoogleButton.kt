package com.adri833.orpheus.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.R
import com.adri833.orpheus.ui.theme.BaseGoogleColors
import com.adri833.orpheus.utils.noRippleClickable

@Composable
fun NeonGoogleButton(
    enabled: Boolean = true,
    isLoading: Boolean = false,
    isSuccessAnimationActive: Boolean = false,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }
    val interactionSource = remember { MutableInteractionSource() }

    val infiniteTransition = rememberInfiniteTransition(label = "colorSweep")
    val offset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

    val shiftedColors = run {
        val size = BaseGoogleColors.size
        val shifted = mutableListOf<Color>()

        for (i in 0 until size - 1) {
            val fIndex = (i + offset.value * (size - 1)) % (size - 1)
            val iLow = fIndex.toInt()
            val iHigh = (iLow + 1) % (size - 1)
            val fraction = fIndex - iLow
            shifted.add(lerp(BaseGoogleColors[iLow], BaseGoogleColors[iHigh], fraction))
        }

        shifted + shifted.first()
    }

    val brush = Brush.sweepGradient(shiftedColors)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .graphicsLayer(rotationZ = offset.value * 0.01f)
            .border(BorderStroke(4.dp, brush), RoundedCornerShape(50))
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(50)
                )
                .noRippleClickable(enabled, onClick)
                .padding(horizontal = 18.dp, vertical = 10.dp)
                .animateContentSize(animationSpec = tween(300)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    alpha = alpha.value
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    modifier = Modifier.size(36.dp),
                    contentDescription = "Google logo"
                )

                if (!isLoading && !isSuccessAnimationActive) {
                    Spacer(modifier = Modifier.width(8.dp))
                } else if (isLoading) {
                    Spacer(modifier = Modifier.width(12.dp))
                }

                if (!isLoading && !isSuccessAnimationActive) {
                    Text(
                        text = stringResource(R.string.login_google),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                } else if (isLoading) {
                    CircularProgressIndicator(
                        color = shiftedColors.last(),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}
