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
    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong

    fun playSong(song: Song) {
        _currentSong.value = song
        playerManager.play(song.contentUri)
    }

    fun pause() {
        playerManager.pause()
    }

    fun resume() {
        playerManager.resume()
    }

    fun isPlaying(): Boolean = playerManager.isPlaying()
}