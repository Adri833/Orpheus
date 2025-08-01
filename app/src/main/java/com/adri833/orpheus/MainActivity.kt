package com.adri833.orpheus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.adri833.orpheus.navigation.NavigationHost
import com.adri833.orpheus.ui.theme.OrpheusTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import android.content.ComponentName
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi


@Suppress("DEPRECATION")
@OptIn(UnstableApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var mediaBrowser: MediaBrowserCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        connectToMediaSession()

        setContent {
            OrpheusTheme {
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color.Black,
                        darkIcons = false
                    )

                    systemUiController.setNavigationBarColor(
                        color = Color.Black,
                        darkIcons = false
                    )
                }
                NavigationHost()
            }
        }
    }

    private fun connectToMediaSession() {
        val mediaBrowser = MediaBrowserCompat(
            this,
            ComponentName(this, com.adri833.orpheus.services.MusicService::class.java),
            object : MediaBrowserCompat.ConnectionCallback() {
                override fun onConnected() {
                    try {
                        val mediaController = MediaControllerCompat(this@MainActivity, mediaBrowser.sessionToken)
                        MediaControllerCompat.setMediaController(this@MainActivity, mediaController)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            },
            null
        )
        mediaBrowser.connect()
    }

}