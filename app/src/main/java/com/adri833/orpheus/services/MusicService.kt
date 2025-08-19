package com.adri833.orpheus.services

import android.app.PendingIntent
import android.media.AudioAttributes as FrameworkAudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.adri833.orpheus.data.player.PlayerManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C

@UnstableApi
@AndroidEntryPoint
class MusicService : MediaSessionService() {
    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var playerManager: PlayerManager

    private var mediaSession: MediaSession? = null

    private lateinit var audioManager: AudioManager
    private lateinit var audioFocusRequest: AudioFocusRequest

    private val focusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                player.volume = 1.0f
                if (!player.isPlaying) {
                    player.play()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                player.pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                player.pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                player.volume = 0.3f
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        player.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .setUsage(C.USAGE_MEDIA)
                .build(),
            true
        )

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                FrameworkAudioAttributes.Builder()
                    .setUsage(FrameworkAudioAttributes.USAGE_MEDIA)
                    .setContentType(FrameworkAudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(focusChangeListener)
            .build()

        val sessionActivityPendingIntent = packageManager
            ?.getLaunchIntentForPackage(packageName)
            ?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        mediaSession = sessionActivityPendingIntent?.let {
            MediaSession.Builder(this, player)
                .setSessionActivity(it)
        }?.build()

        playerManager.attachMusicService(this)
    }

    fun requestAudioFocus(): Boolean {
        val result = audioManager.requestAudioFocus(audioFocusRequest)
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        audioManager.abandonAudioFocusRequest(audioFocusRequest)
        playerManager.release()
        super.onDestroy()
    }
}