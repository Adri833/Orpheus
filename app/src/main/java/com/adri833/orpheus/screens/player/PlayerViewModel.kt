package com.adri833.orpheus.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adri833.orpheus.data.player.PlayerManager
import com.adri833.orpheus.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerManager: PlayerManager
) : ViewModel() {

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _playbackProgress = MutableStateFlow(0f)
    val playbackProgress: StateFlow<Float> = _playbackProgress

    init {
        viewModelScope.launch {
            while (true) {
                val player = playerManager.player
                val progress = if (player.duration > 0)
                    player.currentPosition.toFloat() / player.duration
                else 0f
                _playbackProgress.value = progress.coerceIn(0f, 1f)
                delay(50L)
            }
        }
    }

    fun playSong(song: Song) {
        _currentSong.value = song
        playerManager.play(song.contentUri)
        _isPlaying.value = true
    }

    fun pause() {
        playerManager.pause()
        _isPlaying.value = false
    }

    fun resume() {
        playerManager.resume()
        _isPlaying.value = true
    }
}