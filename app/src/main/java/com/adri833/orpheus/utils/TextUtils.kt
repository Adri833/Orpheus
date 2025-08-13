package com.adri833.orpheus.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.adri833.orpheus.R

@Composable
fun safeText(text: String?): String =
    if (text.isNullOrBlank()) stringResource(R.string.unknown) else text

@Composable
fun NameText(name: String?, color: Color = Color.White, fontSize: Float = 17f, modifier: Modifier = Modifier) {
    Text(
        text = safeText(name),
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun ArtistText(artist: String?, color: Color = Color.Gray, fontSize: Float = 15f, modifier: Modifier = Modifier, fontWeight: FontWeight? = null) {
    Text(
        text = safeText(artist),
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun AlbumText(album: String?, color: Color = Color.Gray, fontSize: Float = 13f, modifier: Modifier = Modifier) {
    Text(
        text = safeText(album),
        fontSize = fontSize.sp,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun OptionText(
    text: String,
    color: Color = Color.White,
    fontSize: Float = 17f,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily(Font(R.font.merriweather_bold, FontWeight.Bold)),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}
