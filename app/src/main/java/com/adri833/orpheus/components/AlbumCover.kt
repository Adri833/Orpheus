package com.adri833.orpheus.components

import android.net.Uri
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

@Composable
fun AlbumCover(
    coverUri: Uri?,
    size: Int = 56
) {
    val painter = if (coverUri != null) {
        rememberAsyncImagePainter(
            model = coverUri,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder)
        )
    } else {
        painterResource(R.drawable.placeholder)
    }

    Image(
        painter = painter,
        contentDescription = "Carátula álbum",
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(10.dp)),
        contentScale = ContentScale.Crop
    )
}