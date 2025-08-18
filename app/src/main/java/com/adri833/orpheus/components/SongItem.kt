package com.adri833.orpheus.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
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
import java.io.File

@Composable
fun SongItem(
    song: Song,
    currentSong: Song?,
    isPlaying: Boolean,
    onClick: (Song) -> Unit,
    onOptionsClick: (Song) -> Unit
) {
    val isCurrent = currentSong?.id == song.id
    val shouldAnimate = isCurrent && isPlaying
    val songFileName = File(song.filePath).name

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
            modifier = Modifier.weight(1f).padding(end = 10.dp)
        ) {
            NameText(
                name = song.title,
                color = if (isCurrent) Gold else Color.White
            )
            ArtistText(artist = song.artist)
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                QualityTag(fileName = songFileName)
                AlbumText(album = song.album)
            }
        }

        // Equalizer
        AnimatedVisibility(
            visible = isCurrent,
            enter = fadeExpandIn(),
            exit = fadeShrinkOut()
        ) {
            NowPlayingEqualizer(
                isPlaying = shouldAnimate,
                modifier = Modifier
                    .size(26.dp)
            )
        }

        // Options button (⋮)
        Box(
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Opciones",
                modifier = Modifier
                    .size(30.dp)
                    .noRippleClickable { onOptionsClick(song) }
            )
        }
    }
}