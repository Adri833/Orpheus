package com.adri833.orpheus.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.adri833.orpheus.data.player.PlayerManager
import com.adri833.orpheus.data.repository.SongRepository
import com.adri833.orpheus.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@UnstableApi
class PlayerViewModel
@Inject constructor(
    val playerManager: PlayerManager,
    songRepository: SongRepository
) : ViewModel() {

    private val _allSongs = MutableStateFlow<List<Song>>(emptyList())
    val currentSong: StateFlow<Song?> = playerManager.currentSong
    val isPlaying: StateFlow<Boolean> = playerManager.isPlaying
    val playbackProgress: StateFlow<Float> = playerManager.progress
    val currentIndex: StateFlow<Int> = playerManager.currentIndex
    val queue: StateFlow<List<Song>> = playerManager.queue
    val isShuffleEnabled: StateFlow<Boolean> = playerManager.isShuffleEnabledFlow
    val songs = songRepository.getSongs()

    private val _selectedSong = MutableStateFlow<Song?>(null)
    val selectedSong = _selectedSong
    private val _isForward = MutableStateFlow(true)
    val isForward: StateFlow<Boolean> = _isForward

    private var lastIndex: Int? = null

    init {
        viewModelScope.launch {
            currentIndex.collect { newIndex ->
                val forward = lastIndex?.let { newIndex > it } ?: true
                _isForward.value = forward
                lastIndex = newIndex
            }
        }
    }

    fun loadSongs(songs: List<Song>) {
        _allSongs.value = songs
    }

    fun onSongSelected(song: Song) {
        val current = currentSong.value
        if (current == song) return
        val all = _allSongs.value
        val startIndex = all.indexOf(song).coerceAtLeast(0)
        playerManager.setQueue(all, startIndex, true)
    }

    fun addSongs(songs: List<Song>) {
        playerManager.addSongs(songs)
    }

    fun removeSongs(songs: List<Song>) {
        playerManager.removeSongs(songs)
    }

    fun reorderQueue(fromIndex: Int, toIndex: Int) {
        playerManager.reorderQueue(fromIndex, toIndex)
    }

    fun selectSong(song: Song) {
        _selectedSong.value = song
    }

    fun toggleShuffle() {
        playerManager.toggleShuffle()
    }

    fun playOrResume(filteredSongs: List<Song>) {
        val currentQueue = playerManager.queue.value
        if (currentQueue.isEmpty()) {
            playerManager.startPlaying(filteredSongs)
        } else {
            if (playerManager.isPlaying.value) {
                playerManager.pause()
            } else {
                playerManager.resume()
            }
        }
    }

    fun togglePlayback() {
        if (playerManager.queue.value.isNotEmpty()) {
            if (playerManager.isPlaying.value) {
                playerManager.pause()
            } else {
                playerManager.resume()
            }
        }
    }


    fun pause() = playerManager.pause()

    fun skipToNext() = playerManager.skipToNext()

    fun skipToPrevious() = playerManager.skipToPrevious()

    fun skipToIndex(index: Int) {
        playerManager.skipToIndex(index)
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}