package com.adri833.orpheus.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.R
import com.adri833.orpheus.ui.theme.BaseGoogleColors

@Composable
fun NeonGoogleButton(
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "colorSweep")
    val offset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
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
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .defaultMinSize(minWidth = 240.dp, minHeight = 56.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    modifier = Modifier.size(36.dp),
                    contentDescription = "Google logo"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.login_google),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}