package com.adri833.orpheus.domain.handler

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.adri833.orpheus.ui.DeniedPermissionUI
import com.adri833.orpheus.utils.getAudioPermission
import com.adri833.orpheus.utils.isAudioPermissionGranted

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermissionHandler(
    contentIfGranted: @Composable () -> Unit
) {
    val context = LocalContext.current

    // 0 = iniciar, 1 = pedir notif, 2 = pedir audio, 3 = permisos concedidos, 4 = denegado
    var step by remember { mutableIntStateOf(0) }
    var notifGranted by remember { mutableStateOf(false) }
    var audioGranted by remember { mutableStateOf(false) }

    val notifLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notifGranted = isGranted
        step = if (isGranted) 2 else 4
    }

    val audioLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        audioGranted = isGranted
        step = if (isGranted) 3 else 4
    }

    LaunchedEffect(Unit) {
        notifGranted = isNotificationPermissionGranted(context)
        audioGranted = isAudioPermissionGranted(context)

        step = when {
            !notifGranted -> 1
            !audioGranted -> 2
            else -> 3
        }
    }

    LaunchedEffect(step) {
        when (step) {
            1 -> notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            2 -> audioLauncher.launch(getAudioPermission())
        }
    }

    when (step) {
        3 -> contentIfGranted()
        4 -> DeniedPermissionUI {
            step = 0
        }
    }
}

fun isNotificationPermissionGranted(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}