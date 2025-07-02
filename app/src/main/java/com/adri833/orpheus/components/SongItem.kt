package com.adri833.orpheus.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.domain.model.Song
import com.adri833.orpheus.utils.AlbumText
import com.adri833.orpheus.utils.ArtistText
import com.adri833.orpheus.utils.NameText
import com.adri833.orpheus.utils.noRippleClickable

@Composable
fun SongItem(
    song: Song,
    onClick: (Song) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .noRippleClickable(
                onClick = { onClick(song) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AlbumCover(song)

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            NameText(name = song.title)
            ArtistText(artist = song.artist)
            AlbumText(album = song.album)
        }
    }
}