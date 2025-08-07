package com.adri833.orpheus.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

suspend fun searchArtistImageUrl(token: String, artistName: String): String? {
    return withContext(Dispatchers.IO) {
        try {
            val url = "https://api.spotify.com/v1/search?q=${artistName}&type=artist&limit=1"
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $token")
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val json = JSONObject(response.body.string())
                val artists = json.getJSONObject("artists").getJSONArray("items")
                if (artists.length() > 0) {
                    val artist = artists.getJSONObject(0)
                    val images = artist.getJSONArray("images")
                    if (images.length() > 0) {
                        images.getJSONObject(0).getString("url")
                    } else null
                } else null
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}