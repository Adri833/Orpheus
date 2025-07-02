package com.adri833.orpheus.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.adri833.orpheus.R
import com.adri833.orpheus.domain.model.Song

@Composable
fun AlbumCover(
    song: Song,
    size: Int = 56
) {
    val painter = rememberAsyncImagePainter(
        model = song.albumArt,
        placeholder = painterResource(R.drawable.placeholder)
    )

    Image(
        painter = painter,
        contentDescription = "Carátula álbum",
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(10.dp)),
        contentScale = ContentScale.Crop
    )
}