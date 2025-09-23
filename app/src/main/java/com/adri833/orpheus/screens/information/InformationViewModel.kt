package com.adri833.orpheus.screens.information

import android.app.Application
import android.app.RecoverableSecurityException
import android.content.Context
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.adri833.orpheus.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val context: Application
) : AndroidViewModel(context) {
    private var pendingSongChanges: Triple<String, String, String>? = null

    private val _uiState = MutableStateFlow(InformationUiState())
    val uiState: StateFlow<InformationUiState> = _uiState

    var currentSong: Song? = null
        private set

    fun loadSong(song: Song) {
        currentSong = song
        _uiState.value = InformationUiState(
            coverUri = song.albumArt,
            title = song.title,
            artist = song.artist,
            album = song.album
        )
    }

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle)
    }

    fun onArtistChange(newArtist: String) {
        _uiState.value = _uiState.value.copy(artist = newArtist)
    }

    fun onAlbumChange(newAlbum: String) {
        _uiState.value = _uiState.value.copy(album = newAlbum)
    }

    fun saveChanges(
        onRecoverableSecurityException: (IntentSender) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        currentSong?.let { song ->
            val title = _uiState.value.title
            val artist = _uiState.value.artist
            val album = _uiState.value.album

            pendingSongChanges = Triple(title, artist, album)

            viewModelScope.launch {
                val success = updateSongMetadata(
                    context, song.filePath,
                    title, artist, album,
                    onRecoverableSecurityException
                )
                if (success) {
                    currentSong = currentSong?.copy(albumArt = _uiState.value.coverUri)

                    pendingSongChanges = null
                    onSuccess()
                }
            }
        }
    }

    fun retryPendingChanges(onRecoverableSecurityException: (IntentSender) -> Unit = {}, onSuccess: () -> Unit = {}) {
        pendingSongChanges?.let { (title, artist, album) ->
            currentSong?.let { song ->
                viewModelScope.launch {
                    val success = updateSongMetadata(
                        context, song.filePath,
                        title, artist, album,
                        onRecoverableSecurityException
                    )
                    if (success) {
                        pendingSongChanges = null
                        onSuccess()
                    }
                }
            }
        }
    }

    private suspend fun updateSongMetadata(
        context: Context,
        filePath: String,
        newTitle: String,
        newArtist: String,
        newAlbum: String,
        onRecoverableSecurityException: (IntentSender) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val uri = getSongUri(context, filePath) ?: return@withContext false
            val extension = File(filePath).extension
            val tempFile = File(context.cacheDir, "temp_song.$extension")

            context.contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output -> input.copyTo(output) }
            }

            val audioFile = AudioFileIO.read(tempFile)
            val tag = audioFile.tagOrCreateAndSetDefault
            tag.setField(FieldKey.TITLE, newTitle)
            tag.setField(FieldKey.ARTIST, newArtist)
            tag.setField(FieldKey.ALBUM, newAlbum)

            AudioFileIO.write(audioFile)

            try {
                context.contentResolver.openFileDescriptor(uri, "rw")?.use { pfd ->
                    FileOutputStream(pfd.fileDescriptor).use { output ->
                        tempFile.inputStream().use { input -> input.copyTo(output) }
                    }
                }
            } catch (e: RecoverableSecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    onRecoverableSecurityException(e.userAction.actionIntent.intentSender)
                }
                tempFile.delete()
                return@withContext false
            }

            tempFile.delete()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getSongUri(context: Context, filePath: String): Uri? {
        val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA)
        val selection = "${MediaStore.Audio.Media.DATA}=?"
        val selectionArgs = arrayOf(filePath)

        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val songId = cursor.getLong(idColumn)
                return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId.toString())
            }
        }
        return null
    }
}