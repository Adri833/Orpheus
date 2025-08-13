package com.adri833.orpheus.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.constants.OrpheusConstants.SPOTIFY_CLIENT_ID
import com.adri833.orpheus.constants.OrpheusConstants.SPOTIFY_CLIENT_SECRET
import com.adri833.orpheus.utils.ArtistText

@Composable
fun ArtistItem(
    artistName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArtistImage(
            artistName = artistName,
            clientId = SPOTIFY_CLIENT_ID,
            clientSecret = SPOTIFY_CLIENT_SECRET,
            modifier = Modifier.size(70.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        ArtistText(
            artist = artistName,
            color = Color.White,
            fontSize = 17f,
            fontWeight = FontWeight.Bold
        )
    }
}