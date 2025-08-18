package com.adri833.orpheus.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getDominantColor(context: Context, imageUri: Uri?): Color {
    if (imageUri == null) return Color.LightGray

    return withContext(Dispatchers.IO) {
        try {
            val bitmap = context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }

            if (bitmap != null) {
                val palette = Palette.from(bitmap).generate()
                val dominantColor = palette.getDominantColor(Color.LightGray.toArgb())
                Color(dominantColor)
            } else {
                Color.LightGray
            }
        } catch (_: Exception) {
            Color.LightGray
        }
    }
}

fun Color.darken(factor: Float): Color {
    return copy(
        red = red * (1 - factor),
        green = green * (1 - factor),
        blue = blue * (1 - factor)
    )
}

fun Color.lighten(factor: Float): Color {
    return copy(
        red = red + (1 - red) * factor,
        green = green + (1 - green) * factor,
        blue = blue + (1 - blue) * factor
    )
}

fun Color.mixWithWhite(factor: Float): Color {
    return Color(
        red = red + (1f - red) * factor,
        green = green + (1f - green) * factor,
        blue = blue + (1f - blue) * factor,
        alpha = alpha
    )
}

fun isColorDark(color: Color): Boolean {
    val r = color.red
    val g = color.green
    val b = color.blue
    val luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b
    val contrastWithWhite = (1.05) / (luminance + 0.05)
    return contrastWithWhite > 3.0  // si el contraste con blanco es alto, es oscuro
}

fun Color.adaptiveProgressBackground(): Color {
    return if (!isColorDark(this)) {
        this.darken(0.2f).copy(alpha = 0.5f)
    } else {
        this.copy(alpha = 0.4f)
    }
}