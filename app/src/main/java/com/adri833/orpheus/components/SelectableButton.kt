package com.adri833.orpheus.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.R
import com.adri833.orpheus.ui.pulseScale
import com.adri833.orpheus.ui.theme.DarkGray
import com.adri833.orpheus.ui.theme.Gold

@Composable
fun SelectableButton(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale = pulseScale(pressed) {
        pressed = false
    }

    Surface(
        color = if (isSelected) Gold else DarkGray,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        pressed = true
                        onClick()
                    }
                )
            }
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(
            text = name,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(R.font.merriweather_regular)),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}