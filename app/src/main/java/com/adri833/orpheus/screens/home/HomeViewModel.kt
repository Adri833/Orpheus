package com.adri833.orpheus.screens.home

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adri833.orpheus.R
import com.adri833.orpheus.data.repository.AuthRepository
import com.adri833.orpheus.data.repository.SongRepository
import com.adri833.orpheus.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val songRepository: SongRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    fun getProfilePicture(): Uri? {
        return authRepository.getProfilePictureUrl()
    }

    fun getUserName(): String? {
        return authRepository.getUserName()
    }

    fun getGreetingMessage(): String {
        val currentHour = ZonedDateTime.now(ZoneId.systemDefault()).hour

        val greeting = when (currentHour) {
            in 6..11 -> context.getString(R.string.good_morning)
            in 12..20 -> context.getString(R.string.good_afternoon)
            else -> context.getString(R.string.good_night)
        }

        return "$greeting, ${getUserName() ?: ""}"
    }

    fun logout() {
        authRepository.logout()
    }

    fun loadSongs() {
        viewModelScope.launch {
            val list = songRepository.getSongs()
            _songs.value = list
        }
    }
}