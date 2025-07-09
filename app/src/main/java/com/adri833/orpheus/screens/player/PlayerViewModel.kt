package com.adri833.orpheus.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adri833.orpheus.data.player.PlayerManager
import com.adri833.orpheus.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
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

    val isForward: StateFlow<Boolean> = currentIndex
        .runningFold(Pair(-1, true)) { (lastIndex, _), newIndex ->
            val forward = if (lastIndex == -1) true else newIndex > lastIndex
            newIndex to forward
        }
        .map { it.second }
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun loadSongs(songs: List<Song>) {
        _allSongs.value = songs
    }

    fun onSongSelected(song: Song) {
        val current = currentSong.value
        if (current == song) return
        val all = _allSongs.value
        val startIndex = all.indexOf(song).coerceAtLeast(0)
        playerManager.setQueue(all, startIndex)
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }

    fun playOrResume() {
        val current = currentSong.value
        if (current == null) {
            val songs = _allSongs.value
            if (songs.isNotEmpty()) {
                playerManager.setQueue(songs)
            }
        } else {
            playerManager.resume()
        }
    }

    fun pause() = playerManager.pause()
    fun skipToNext() = playerManager.skipToNext()
    fun skipToPrevious() = playerManager.skipToPrevious()
}