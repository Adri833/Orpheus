package com.adri833.orpheus.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.adri833.orpheus.R
import androidx.media.app.NotificationCompat.MediaStyle
import com.adri833.orpheus.data.player.PlayerManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MusicService : Service() {

    @Inject lateinit var playerManager: PlayerManager
    private lateinit var mediaSession: MediaSessionCompat

    companion object {
        const val CHANNEL_ID = "music_channel"
        const val NOTIFICATION_ID = 1

        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        mediaSession = MediaSessionCompat(this, "MusicService").apply {
            isActive = true
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    playerManager.resume()
                }

                override fun onPause() {
                    playerManager.pause()
                }

                override fun onSkipToNext() {
                    playerManager.skipToNext()
                }

                override fun onSkipToPrevious() {
                    playerManager.skipToPrevious()
                }

                override fun onStop() {
                    stopSelf()
                }
            })

            setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setActions(
                        PlaybackStateCompat.ACTION_PLAY or
                                PlaybackStateCompat.ACTION_PAUSE or
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                                PlaybackStateCompat.ACTION_STOP
                    )
                    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
                    .build()
            )
        }
        playerManager.attachMusicService(this)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Reproducción de música",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        startForeground(NOTIFICATION_ID, createNotification())
    }

    fun updateMetadata(title: String, artist: String, albumArt: Bitmap?) {
        val metadataBuilder = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)

        albumArt?.let {
            metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, it)
        }

        mediaSession.setMetadata(metadataBuilder.build())

        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY or
                            PlaybackStateCompat.ACTION_PAUSE or
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                            PlaybackStateCompat.ACTION_STOP
                )
                .setState(
                    if (playerManager.player.isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                    playerManager.player.currentPosition,
                    1f
                )
                .build()
        )
        updateNotification()
    }

    // Nuevo método para recibir el estado del PlayerManager y actuar en consecuencia
    fun handlePlaybackState(isPlaying: Boolean) {
        if (isPlaying) {
            // El reproductor está activo, asegúrate de que el servicio esté en primer plano
            startForegroundService()
        } else {
            // El reproductor está en pausa, puedes salir del modo primer plano,
            // pero la notificación debe permanecer visible
            stopForeground(STOP_FOREGROUND_DETACH)
            updateNotification()
        }
    }

    private fun updateNotification() {
        val notification = createNotification()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotification(): Notification {
        val mediaMetadata = mediaSession.controller.metadata

        val playPauseIcon = if (playerManager.player.isPlaying) R.drawable.ic_pause else R.drawable.ic_play

        val playPauseAction = NotificationCompat.Action(
            playPauseIcon,
            if (playerManager.player.isPlaying) "Pause" else "Play",
            getPendingIntent(if (playerManager.player.isPlaying) ACTION_PAUSE else ACTION_PLAY)
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(mediaMetadata?.getString(MediaMetadataCompat.METADATA_KEY_TITLE) ?: "App de Música")
            .setContentText(mediaMetadata?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST) ?: "Reproduciendo música")
            .setLargeIcon(mediaMetadata?.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART))
            .setSmallIcon(R.drawable.ic_google)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setStyle(
                MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(getPendingIntent(ACTION_STOP))
            )
            .addAction(NotificationCompat.Action(R.drawable.ic_previous, "Anterior", getPendingIntent(ACTION_PREVIOUS)))
            .addAction(playPauseAction)
            .addAction(NotificationCompat.Action(R.drawable.ic_next, "Siguiente", getPendingIntent(ACTION_NEXT)))
            .setDeleteIntent(getPendingIntent(ACTION_STOP))
            .setOngoing(playerManager.player.isPlaying)
            .build()
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply { this.action = action }
        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> mediaSession.controller.transportControls.play()
            ACTION_PAUSE -> mediaSession.controller.transportControls.pause()
            ACTION_NEXT -> mediaSession.controller.transportControls.skipToNext()
            ACTION_PREVIOUS -> mediaSession.controller.transportControls.skipToPrevious()
            ACTION_STOP -> {
                mediaSession.controller.transportControls.stop()
                stopForeground(true) // STOP_FOREGROUND_REMOVE
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        playerManager.player.release()
        mediaSession.release()
        super.onDestroy()
    }
}