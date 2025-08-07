package com.adri833.orpheus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adri833.orpheus.network.getSpotifyToken
import com.adri833.orpheus.network.searchArtistImageUrl

@Composable
fun ArtistImage(
    artistName: String,
    clientId: String,
    clientSecret: String,
    modifier: Modifier = Modifier.size(100.dp)
) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(artistName) {
        val token = getSpotifyToken(clientId, clientSecret)
        if (token != null) {
            val url = searchArtistImageUrl(token, artistName)
            imageUrl = url
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
            // Placeholder en caso de no encontrar imagen
            Box(
                modifier = modifier
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No Img", color = Color.White)
            }
        }
    }
}
