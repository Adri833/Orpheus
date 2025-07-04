package com.adri833.orpheus.screens.player

import androidx.lifecycle.ViewModel
import com.adri833.orpheus.data.player.PlayerManager
import com.adri833.orpheus.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerManager: PlayerManager
) : ViewModel() {

    private val _allSongs = MutableStateFlow<List<Song>>(emptyList())

    val currentSong: StateFlow<Song?> = playerManager.currentSong
    val isPlaying: StateFlow<Boolean> = playerManager.isPlaying
    val playbackProgress: StateFlow<Float> = playerManager.progress
    val currentIndex: StateFlow<Int> = playerManager.currentIndex
    val queue: StateFlow<List<Song>> = playerManager.queue
    fun loadSongs(songs: List<Song>) {
        _allSongs.value = songs
    }

    fun onSongSelected(song: Song, shuffle: Boolean = false) {
        buildQueueForSong(song, shuffle)
    }

    private fun buildQueueForSong(selectedSong: Song, shuffle: Boolean) {
        val all = _allSongs.value

        val newQueue = if (shuffle) {
            (all - selectedSong).shuffled().toMutableList().apply { add(0, selectedSong) }
        } else {
            val index = all.indexOf(selectedSong)
            if (index != -1) all.subList(index, all.size) else listOf(selectedSong)
        }

        playerManager.setQueue(newQueue, 0)
    }

    fun pause() = playerManager.pause()
    fun resume() = playerManager.resume()
    fun skipToNext() = playerManager.skipToNext()
    fun skipToPrevious() = playerManager.skipToPrevious()

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}