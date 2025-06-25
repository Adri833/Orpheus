package com.adri833.orpheus.domain.model

import android.net.Uri

data class Song(
    val id: Long,
    val title: String,
    val album: String,
    val artist: String,
    val albumArt: Uri?,
    val contentUri: Uri,
)