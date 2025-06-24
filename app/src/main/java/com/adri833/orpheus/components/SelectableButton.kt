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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.ui.pulseScaleSlim
import com.adri833.orpheus.ui.theme.Gold

@Composable
fun SelectableButton(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale = pulseScaleSlim(pressed) {
        pressed = false
    }

    Surface(
        color = if (isSelected) Gold else Color(0xFF2C2C2C),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}