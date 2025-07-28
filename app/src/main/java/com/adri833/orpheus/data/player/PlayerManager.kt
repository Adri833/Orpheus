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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val player: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            addListener(playerListener)
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
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

    private val _isShuffleEnabledFlow = MutableStateFlow(false)
    val isShuffleEnabledFlow: StateFlow<Boolean> = _isShuffleEnabledFlow

    private var originalQueue: MutableList<Song> = mutableListOf()
    private var currentQueue: MutableList<Song> = mutableListOf()
    private var isShuffleEnabled = false
    private var progressJob: Job? = null

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val index = player.currentMediaItemIndex
            _currentIndex.value = index
            _currentSong.value = currentQueue.getOrNull(index)
            _queue.value = currentQueue.toList()
        }
    }

    fun pause() = player.pause()

    fun resume() = player.play()

    fun skipToNext() {
        player.seekToNext()
        player.play()
    }

    fun skipToIndex(index: Int) {
        if (index in currentQueue.indices) {
            player.seekTo(index, 0L)
            _currentIndex.value = index
            player.play()
        }
    }


    fun skipToPrevious() {
        val index = player.currentMediaItemIndex
        player.seekTo(if (index > 0) index - 1 else 0, 0L)
        player.play()
    }

    fun release() {
        player.removeListener(playerListener)
        player.stop()
        player.release()
        stopProgressUpdates()
        scope.cancel()
    }

    fun setQueue(songs: List<Song>, startIndex: Int = 0, autoPlay: Boolean = false) {
        originalQueue = songs.toMutableList()
        val selectedSong = songs.getOrNull(startIndex)

        currentQueue = if (!isShuffleEnabled) {
            songs.toMutableList()
        } else {
            val shuffled = songs.toMutableList().apply { remove(selectedSong) }.shuffled()
            mutableListOf<Song>().apply {
                selectedSong?.let { add(it) }
                addAll(shuffled)
            }
        }

        _queue.value = currentQueue.toList()
        val newStartIndex = selectedSong?.let { currentQueue.indexOf(it) } ?: 0
        setQueueInternal(newStartIndex, autoPlay)
    }

    private fun setQueueInternal(startIndex: Int, autoPlay: Boolean = false) {
        _queue.value = currentQueue.toList()

        val mediaItems = currentQueue.map { it.toMediaItem() }
        val validIndex = startIndex.coerceIn(0, currentQueue.lastIndex)

        player.setMediaItems(mediaItems, validIndex, 0L)
        player.prepare()
        if (autoPlay) player.play()

        startProgressUpdates()
    }

    fun toggleShuffle() {
        isShuffleEnabled = !isShuffleEnabled
        _isShuffleEnabledFlow.value = isShuffleEnabled

        if (originalQueue.isEmpty()) return

        val currentSong = currentQueue.getOrNull(player.currentMediaItemIndex)
        val currentPosition = player.currentPosition

        currentQueue = if (isShuffleEnabled) {
            val remainingSongs = originalQueue.filter { it != currentSong }.shuffled()
            mutableListOf<Song>().apply {
                currentSong?.let { add(it) }
                addAll(remainingSongs)
            }
        } else {
            originalQueue.toMutableList()
        }

        val startIndex = currentSong?.let { currentQueue.indexOf(it) } ?: 0
        setQueueInternal(startIndex, autoPlay = player.isPlaying)
        player.seekTo(startIndex, currentPosition)
    }

    fun addSongs(songsToAdd: List<Song>) {
        originalQueue.addAll(songsToAdd)

        currentQueue = if (isShuffleEnabled) {
            val shuffledNew = songsToAdd.shuffled()
            (currentQueue + shuffledNew).toMutableList()
        } else {
            (currentQueue + songsToAdd).toMutableList()
        }

        val currentIndex = player.currentMediaItemIndex
        setQueueInternal(currentIndex)
    }

    fun removeSongs(songsToRemove: List<Song>) {
        originalQueue.removeAll(songsToRemove)
        currentQueue.removeAll(songsToRemove)

        if (currentQueue.isEmpty()) {
            player.stop()
            player.clearMediaItems()
            _queue.value = emptyList()
            _currentSong.value = null
            _currentIndex.value = 0
            stopProgressUpdates()
            return
        }

        val currentIndex = player.currentMediaItemIndex.coerceAtMost(currentQueue.lastIndex)
        setQueueInternal(currentIndex)
    }

    fun reorderQueue(fromIndex: Int, toIndex: Int) {
        if (isShuffleEnabled) return

        val song = currentQueue.removeAt(fromIndex)
        currentQueue.add(toIndex, song)

        originalQueue = currentQueue.toMutableList()

        val currentIndex = player.currentMediaItemIndex
        setQueueInternal(currentIndex)
    }

    private fun startProgressUpdates() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (true) {
                val dur = player.duration
                val pos = player.currentPosition
                _progress.value = if (dur > 0) pos.toFloat() / dur else 0f
                delay(100L)
            }
        }
    }

    fun startPlaying(songsList: List<Song>) {
        if (queue.value.isEmpty()) {
            val startIndex = if (isShuffleEnabled && songsList.isNotEmpty()) {
                (songsList.indices).random()
            } else {
                0
            }
            setQueue(songsList, startIndex = startIndex)
            player.play()
        } else {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }


    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun Song.toMediaItem(): MediaItem = MediaItem.fromUri(contentUri)
}