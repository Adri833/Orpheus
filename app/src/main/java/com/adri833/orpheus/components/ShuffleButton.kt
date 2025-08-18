package com.adri833.orpheus.components

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.adri833.orpheus.R
import com.adri833.orpheus.screens.player.PlayerViewModel
import com.adri833.orpheus.ui.theme.Gold
import com.adri833.orpheus.ui.theme.DarkGray
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.adri833.orpheus.utils.noRippleClickable


@OptIn(UnstableApi::class)
@Composable
fun ShuffleButton(
    playerViewModel: PlayerViewModel,
    size: Int = 40,
    colorEnabled: Color = Gold,
    colorDisabled: Color = DarkGray
) {
    val isShuffleEnabled by playerViewModel.isShuffleEnabled.collectAsState()

    Icon(
        painter = painterResource(id = R.drawable.ic_shuffle),
        contentDescription = stringResource(R.string.shuffle),
        tint = if (isShuffleEnabled) colorEnabled else colorDisabled,
        modifier = Modifier
            .size(size.dp)
            .noRippleClickable { playerViewModel.toggleShuffle() }
    )
}