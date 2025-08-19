package com.adri833.orpheus.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.R

@Composable
fun QualityTag(
    fileName: String,
    size: Int
) {
    val highQualityExtensions = listOf("FLAC", "M4A", "ALAC", "WAV")
    val extension = fileName.substringAfterLast('.', "").uppercase()
    val isHighQuality = extension in highQualityExtensions

    if (isHighQuality) {
        Image(
            painter = painterResource(id = R.drawable.hi_res_tag),
            contentDescription = "High Quality",
            modifier = Modifier
                .padding(start = 1.dp, end = 4.dp)
                .size(size.dp)
        )
    }
}