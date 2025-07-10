package com.adri833.orpheus.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.adri833.orpheus.R
import com.adri833.orpheus.screens.player.PlayerViewModel
import com.adri833.orpheus.ui.theme.Gold
import com.adri833.orpheus.ui.theme.LightGray
import androidx.compose.runtime.getValue


@Composable
fun ShuffleButton(
    playerViewModel: PlayerViewModel
) {
    val isShuffleEnabled by playerViewModel.isShuffleEnabled.collectAsState()

    IconButton(
        onClick = { playerViewModel.toggleShuffle() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_shuffle),
            contentDescription = stringResource(R.string.shuffle),
            tint = if (isShuffleEnabled) Gold else LightGray
        )
    }
}