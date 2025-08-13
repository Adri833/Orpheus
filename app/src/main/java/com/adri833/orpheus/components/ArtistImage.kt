package com.adri833.orpheus.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.adri833.orpheus.data.repository.getArtistImage
import com.adri833.orpheus.data.repository.saveArtistImage
import com.adri833.orpheus.network.getSpotifyToken
import com.adri833.orpheus.network.searchArtistImageUrl
import com.adri833.orpheus.R

@Composable
fun ArtistImage(
    artistName: String,
    clientId: String,
    clientSecret: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(artistName) {
        val cachedUrl = getArtistImage(context, artistName)
        if (cachedUrl != null) {
            imageUrl = cachedUrl
        } else {
            val token = getSpotifyToken(clientId, clientSecret)
            if (token != null) {
                val url = searchArtistImageUrl(token, artistName)
                imageUrl = url
                if (url != null) saveArtistImage(context, artistName, url)
            }
        }
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = modifier)
    } else {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Foto de $artistName",
                modifier = modifier.clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            AsyncImage(
                model = R.drawable.placeholder,
                contentDescription = "Placeholder de $artistName",
                modifier = modifier.clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}
