package com.adri833.orpheus.screens.drive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.adri833.orpheus.utils.adjustForMobile

@Composable
fun DriveScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .adjustForMobile(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Pantalla Drive",
            color = Color.White
        )
    }
}