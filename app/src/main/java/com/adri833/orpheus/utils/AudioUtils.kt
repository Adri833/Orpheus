package com.adri833.orpheus.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

fun getAudioPermission(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}

fun isAudioPermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        getAudioPermission()
    ) == PackageManager.PERMISSION_GRANTED
}

