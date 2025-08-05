package com.adri833.orpheus.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.utils.OptionText
import com.adri833.orpheus.utils.noRippleClickable

@Composable
fun OptionListItem(
    icon: Painter,
    text: String,
    color: Color = Color.White,
    size: Int = 24,
    onClick: () -> Unit
) {
    ListItem(
        leadingContent = {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(size.dp)
            )
        },
        headlineContent = {
            OptionText(text = text, color = color)
        },
        modifier = Modifier.noRippleClickable { onClick() }
    )
}