package com.adri833.orpheus.screens.home

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
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
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadSongs()
    }

    private val _selectedAlbum = MutableStateFlow<String?>(null)
    val selectedAlbum: StateFlow<String?> get() = _selectedAlbum

    fun selectAlbum(albumName: String) {
        _selectedAlbum.value = albumName
    }

    private val _selectedArtist = MutableStateFlow<String?>(null)
    val selectedArtist: StateFlow<String?> get() = _selectedArtist

    fun selectArtist(artistName: String) {
        _selectedArtist.value = artistName
    }

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

    fun refreshSongs() {
        viewModelScope.launch {
            val list = songRepository.getSongs()
            _songs.value = list
        }
    }

    fun loadSongs() {
        viewModelScope.launch {
            val list = songRepository.getSongs()
            _songs.value = list
        }
    }

    fun getFolders(): List<String> {
        return songRepository.getFolders()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun shareSong(context: Context, song: Song) {
        val uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            song.id
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "audio/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.share)
            )
        )
    }

    fun deleteSong(
        song: Song,
        onRecoverableSecurityException: (IntentSender) -> Unit,
        onPendingDelete: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            val uri = song.contentUri
            try {
                val deleted = context.contentResolver.delete(uri, null, null) > 0

                if (deleted) {
                    Toast.makeText(context, context.getString(R.string.song_deleted_success), Toast.LENGTH_SHORT).show()
                    loadSongs()
                } else {
                    Toast.makeText(context, context.getString(R.string.song_deleted_failure), Toast.LENGTH_SHORT).show()
                }
            } catch (e: SecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && e is RecoverableSecurityException) {
                    onPendingDelete?.invoke()
                    val intentSender = e.userAction.actionIntent.intentSender
                    onRecoverableSecurityException(intentSender)
                } else {
                    Toast.makeText(context, context.getString(R.string.song_deleted_failure), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}