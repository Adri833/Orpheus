package com.adri833.orpheus.screens.information

import android.net.Uri

data class InformationUiState(
    val coverUri: Uri? = null,
    val title: String = "",
    val artist: String = "",
    val album: String = ""
)
