package com.adri833.orpheus.domain.handler

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.adri833.orpheus.ui.DeniedPermissionUI
import com.adri833.orpheus.utils.getAudioPermission
import com.adri833.orpheus.utils.isAudioPermissionGranted

@Composable
fun AudioPermissionHandler(
    contentIfGranted: @Composable () -> Unit
) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    var requestedOnce by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = isAudioPermissionGranted(context)

            permissionGranted = granted

            if (!granted && !requestedOnce) {
                launcher.launch(getAudioPermission())
                requestedOnce = true
            }
        } else {
            permissionGranted = true
        }
    }

    if (permissionGranted) {
        contentIfGranted()
    } else {
        DeniedPermissionUI(onPermissionGranted = { permissionGranted = true })
    }
}