package com.adri833.orpheus.ui.theme

import android.content.Context
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.adri833.orpheus.utils.getDominantColor
import com.adri833.orpheus.utils.isColorDark

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Google Colors
val BaseGoogleColors = listOf(
    Color(0xFF4285F4),
    Color(0xFFDB4437),
    Color(0xFFF4B400),
    Color(0xFF0F9D58),
    Color(0xFF4285F4),
)

data class SongColors(
    val backgroundColor: Color,
    val contentColor: Color
)
// Devuelve los colores de fondo y de contenido basados en la portada de la canción.
@Composable
fun rememberSongColors(context: Context, albumArtUri: Uri?): SongColors {
    var targetColor by remember { mutableStateOf(Color.LightGray) }

    val dominantColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 200)
    )

    val contentColor = if (isColorDark(dominantColor)) Color.White else Color.Black

    LaunchedEffect(albumArtUri) {
        albumArtUri?.let {
            targetColor = getDominantColor(context, it)
        }
    }

    val backgroundColor = Color(
        red = dominantColor.red,
        green = dominantColor.green,
        blue = dominantColor.blue,
        alpha = 1f
    )

    return SongColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}

val Gold = Color(0xFFD6A75C)

// Grayscale palette (light → dark)
val LightGray = Color(0xFFAAAAAA)
val MediumGray = Color(0xFF888888)
val DarkGray = Color(0xFF5F5F5F)
val VeryDarkGray = Color(0xFF2C2C2C)
val NearBlack = Color(0xFF1C1C1C)
val BackgroundDark = Color(0xFF121212)