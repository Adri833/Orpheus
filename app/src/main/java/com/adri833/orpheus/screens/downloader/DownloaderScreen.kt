package com.adri833.orpheus.screens.downloader

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.adri833.orpheus.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DownloaderScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        view?.post {
                            view.scrollTo(0, 0)
                        }
                    }
                }

                setDownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
                    val request = DownloadManager.Request(url.toUri()).apply {
                        setMimeType(mimeType)
                        addRequestHeader("User-Agent", userAgent)
                        setDescription("Descargando archivo...")
                        val fileName = contentDisposition.substringAfter("filename=", "archivo.pdf")
                            .replace("\"", "")
                        setTitle(fileName)
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, fileName)
                    }

                    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val downloadId = dm.enqueue(request)

                    Toast.makeText(context, context.getString(R.string.downloading), Toast.LENGTH_SHORT).show()

                    scope.launch(Dispatchers.IO) {
                        var isDownloading = true
                        while (isDownloading) {
                            val query = DownloadManager.Query().setFilterById(downloadId)
                            val cursor = dm.query(query)

                            if (cursor.moveToFirst()) {
                                val statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                                if (statusColumnIndex != -1) {
                                    val status = cursor.getInt(statusColumnIndex)
                                    when (status) {
                                        DownloadManager.STATUS_SUCCESSFUL -> {
                                            isDownloading = false
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(context, context.getString(R.string.download_success), Toast.LENGTH_LONG).show()
                                            }
                                        }
                                        DownloadManager.STATUS_FAILED -> {
                                            isDownloading = false
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(context, context.getString(R.string.download_error), Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                }
                            }
                            cursor.close()
                            delay(500L)
                        }
                    }
                }

                loadUrl("https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&ved=2ahUKEwiGyYbe0fmRAxWz3QIHHXZFAAsQFnoECAwQAQ&url=https%3A%2F%2Fezconv.cc%2F&usg=AOvVaw2PTC4Mhl-83YFsTRP8pwYC&opi=89978449")
        }
    })
}