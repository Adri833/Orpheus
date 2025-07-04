package com.adri833.orpheus.data.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.adri833.orpheus.domain.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val player: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            addListener(playerListener)
        }
    }

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _queue = MutableStateFlow<List<Song>>(emptyList())
    val queue: StateFlow<List<Song>> = _queue

    private var songQueue: List<Song> = emptyList()
    private var progressJob: Job? = null

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val index = player.currentMediaItemIndex
            _currentIndex.value = index
            _currentSong.value = songQueue.getOrNull(index)

            if (index in songQueue.indices) {
                _queue.value = songQueue.subList(index, songQueue.size)
            } else {
                _queue.value = emptyList()
            }
        }
    }

    fun setQueue(songs: List<Song>, startIndex: Int = 0) {
        songQueue = songs
        _queue.value = songs
        val mediaItems = songs.map { it.toMediaItem() }
        val validIndex = startIndex.coerceIn(0, songs.lastIndex)

        player.setMediaItems(mediaItems, validIndex, 0L)
        player.prepare()
        player.play()

        startProgressUpdates()
    }

    fun pause() = player.pause()
    fun resume() = player.play()
    fun skipToNext() = player.seekToNext()
    fun skipToPrevious() = player.seekToPrevious()

    fun release() {
        player.removeListener(playerListener)
        player.stop()
        player.release()
        stopProgressUpdates()
    }

    private fun startProgressUpdates() {
        progressJob?.cancel()
        progressJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                val dur = player.duration
                val pos = player.currentPosition
                _progress.value = if (dur > 0) pos.toFloat() / dur else 0f
                delay(100L)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
    }

    private fun Song.toMediaItem(): MediaItem = MediaItem.fromUri(this.contentUri)
}