package com.adri833.orpheus.data.repository

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.adri833.orpheus.domain.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.net.toUri
import java.io.File

class SongRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
     fun getSongs(): List<Song> {
        val songs = mutableListOf<Song>()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

         val selection = "${MediaStore.Audio.Media.DATA} LIKE ? OR ${MediaStore.Audio.Media.DATA} LIKE ?"
         val selectionArgs = arrayOf("%/Music/%", "%/Download/%")


         val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATA
        )

        val cursor = context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val album = it.getString(albumColumn)
                val artist = it.getString(artistColumn)
                val albumId = it.getLong(albumIdColumn)

                val contentUri = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
                val albumArtUri = "content://media/external/audio/albumart/$albumId".toUri()
                val filePath = it.getString(dataColumn)

                songs.add(Song(id, title, album, artist, albumArtUri, contentUri, filePath))
            }
        }
        return songs
    }

    fun getFolders(): List<String> {
        val folders = mutableSetOf<String>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val selection = "${MediaStore.Audio.Media.DATA} LIKE ? OR ${MediaStore.Audio.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%/Music/%", "%/Download/%")

        val projection = arrayOf(MediaStore.Audio.Media.DATA)

        val cursor = context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (it.moveToNext()) {
                val filePath = it.getString(dataIndex)
                val folderPath = File(filePath).parentFile?.absolutePath
                if (!folderPath.isNullOrEmpty()) {
                    folders.add(folderPath)
                }
            }
        }

        return folders.toList().sorted()
    }

}