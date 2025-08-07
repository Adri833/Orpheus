package com.adri833.orpheus.network

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

suspend fun getSpotifyToken(clientId: String, clientSecret: String): String? {
    return withContext(Dispatchers.IO) {
        try {
            val url = "https://accounts.spotify.com/api/token"
            val client = OkHttpClient()

            val credentials = "$clientId:$clientSecret"
            val basicAuth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

            val body = "grant_type=client_credentials".toRequestBody("application/x-www-form-urlencoded".toMediaType())

            val request = Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", basicAuth)
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val json = JSONObject(response.body.string())
                json.getString("access_token")
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}