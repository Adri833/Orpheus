package com.adri833.orpheus.screens.information

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.adri833.orpheus.domain.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(
    song: Song,
    viewModel: InformationViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var pendingLauncher by remember { mutableStateOf<((IntentSenderRequest) -> Unit)?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Llamamos a retryPendingChanges
            viewModel.retryPendingChanges(
                onRecoverableSecurityException = { intentSender ->
                    // Usamos pendingLauncher para lanzar el IntentSender
                    pendingLauncher?.invoke(IntentSenderRequest.Builder(intentSender).build())
                },
                onSuccess = {
                    Toast.makeText(context, "Cambios guardados", Toast.LENGTH_SHORT).show()
                    onBack()
                }
            )
        }
    }

    LaunchedEffect(song) {
        viewModel.loadSong(song)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Información") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = uiState.coverUri,
                contentDescription = "Carátula",
                modifier = Modifier
                    .size(150.dp)
            )

            OutlinedTextField(
                value = uiState.title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.artist,
                onValueChange = { viewModel.onArtistChange(it) },
                label = { Text("Artista") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.album,
                onValueChange = { viewModel.onAlbumChange(it) },
                label = { Text("Álbum") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.saveChanges(
                        onRecoverableSecurityException = { intentSender ->
                            launcher.launch(IntentSenderRequest.Builder(intentSender).build())
                        },
                        onSuccess = {
                            Toast.makeText(context, "Cambios guardados", Toast.LENGTH_SHORT).show()

                            onBack()
                        }
                    )
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }
}
