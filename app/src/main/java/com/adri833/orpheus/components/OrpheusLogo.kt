package com.adri833.orpheus.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.adri833.orpheus.R

@Composable
fun OrpheusLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.orpheus_logo_sinfondo),
        contentDescription = "Orpheus Logo",
        modifier = modifier
    )
}
