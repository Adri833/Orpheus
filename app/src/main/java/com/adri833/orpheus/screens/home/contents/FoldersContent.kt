package com.adri833.orpheus.screens.home.contents

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.adri833.orpheus.components.FolderItem
import com.adri833.orpheus.screens.home.HomeViewModel
import com.adri833.orpheus.screens.player.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(UnstableApi::class)
@Composable
fun FoldersContent(
    homeViewModel: HomeViewModel,
    playerViewModel: PlayerViewModel
) {
    val folders = remember { mutableStateListOf<String>() }
    val selectedFolder = remember { mutableStateOf<String?>(null) }
    val songs = homeViewModel.songs.collectAsState(initial = emptyList()).value

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val result = homeViewModel.getFolders()
            folders.clear()
            folders.addAll(result)
        }
    }

    selectedFolder.value?.let { folderPath ->
        val songsInFolder = songs.filter { song ->
            song.filePath.startsWith(folderPath)
        }

        SongsContent(
            songs = songsInFolder,
            homeViewModel = homeViewModel,
            playerViewModel = playerViewModel,
            onBack = { selectedFolder.value = null },
            navigationToInformation = { }
        )
    } ?: run {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(folders) { folder ->
                FolderItem(
                    folderName = File(folder).name,
                    onClick = { selectedFolder.value = folder }
                )
            }
        }
    }
}