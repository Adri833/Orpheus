package com.adri833.orpheus.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.domain.model.Song

@Composable
fun AlbumItem(
    albumName: String,
    song : Song,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(top = 24.dp, start = 8.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AlbumCoverWithVinyl(song)

        Spacer(modifier = Modifier.width(28.dp))

        Text(
            text = albumName,
            style = MaterialTheme.typography.titleMedium
        )
    }
}