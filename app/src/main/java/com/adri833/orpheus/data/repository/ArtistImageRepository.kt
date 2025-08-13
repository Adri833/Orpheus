package com.adri833.orpheus.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.artistImageDataStore by preferencesDataStore("artist_images")

suspend fun saveArtistImage(context: Context, artistName: String, url: String) {
    context.artistImageDataStore.edit { prefs ->
        prefs[stringPreferencesKey(artistName)] = url
    }
}

suspend fun getArtistImage(context: Context, artistName: String): String? {
    val prefs = context.artistImageDataStore.data.first()
    return prefs[stringPreferencesKey(artistName)]
}
