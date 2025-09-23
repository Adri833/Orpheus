package com.adri833.orpheus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.R
import com.adri833.orpheus.ui.theme.Gold
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.adri833.orpheus.utils.noRippleClickable

@Composable
fun PlayButton(
    isPlaying: StateFlow<Boolean>,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Gold,
    iconColor: Color = Color.Black
) {
    val playing by isPlaying.collectAsState()

    Box(
        modifier = modifier
            .size(54.dp)
            .background(color = backgroundColor, shape = CircleShape)
            .clip(CircleShape)
            .noRippleClickable {
                if (playing) onPauseClick() else onPlayClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(
                id = if (playing) R.drawable.ic_pause else R.drawable.ic_play
            ),
            contentDescription = if (playing) "Pause" else "Play",
            tint = iconColor,
            modifier = Modifier.size(32.dp)
        )
    }
}