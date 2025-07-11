package com.adri833.orpheus.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.domain.model.Song
import com.adri833.orpheus.ui.fadeExpandIn
import com.adri833.orpheus.ui.fadeShrinkOut
import com.adri833.orpheus.ui.theme.Gold
import com.adri833.orpheus.utils.AlbumText
import com.adri833.orpheus.utils.ArtistText
import com.adri833.orpheus.utils.NameText
import com.adri833.orpheus.utils.noRippleClickable

@Composable
fun SongItem(
    song: Song,
    currentSong: Song?,
    isPlaying: Boolean,
    onClick: (Song) -> Unit
) {
    val isCurrent = currentSong?.id == song.id
    val shouldAnimate = isCurrent && isPlaying

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .noRippleClickable { onClick(song) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AlbumCover(song)

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            NameText(
                name = song.title,
                color = if (isCurrent) Gold else Color.White
            )
            ArtistText(artist = song.artist)
            AlbumText(album = song.album)
        }

        AnimatedVisibility(
            visible = isCurrent,
            enter = fadeExpandIn(),
            exit = fadeShrinkOut()
        ) {
            NowPlayingEqualizer(
                isPlaying = shouldAnimate,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(24.dp)
            )
        }
    }
}