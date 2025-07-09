package com.adri833.orpheus.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import com.adri833.orpheus.ui.slidingOffset
import kotlin.math.roundToInt

@Composable
fun SlidingText(
    text: String,
    color: Color,
    fontWeight: FontWeight,
    fontSize: TextUnit,
    forward: Boolean = true,
) {
    var displayText by remember { mutableStateOf(text) }
    var oldText by remember { mutableStateOf(text) }

    val offsetX = slidingOffset(key = text, forward = forward)

    LaunchedEffect(text) {
        if (text != oldText) {
            displayText = text
            oldText = text
        }
    }

    Text(
        text = displayText,
        fontWeight = fontWeight,
        color = color,
        fontSize = fontSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .fillMaxWidth()
    )
}

