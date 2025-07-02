package com.adri833.orpheus.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text

@Composable
fun NameText(name: String, color: Color = Color.White, modifier: Modifier = Modifier) {
    Text(
        text = name,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun ArtistText(artist: String, color: Color = Color.Gray, modifier: Modifier = Modifier) {
    Text(
        text = artist,
        fontSize = 15.sp,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun AlbumText(album: String, color: Color = Color.Gray, modifier: Modifier = Modifier) {
    Text(
        text = album,
        fontSize = 13.sp,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}