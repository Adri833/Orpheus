package com.adri833.orpheus.screens.information

import android.app.Activity
import android.net.Uri
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adri833.orpheus.components.AlbumCover
import com.adri833.orpheus.domain.model.Song
import com.adri833.orpheus.ui.theme.Gold
import com.adri833.orpheus.R
import com.adri833.orpheus.utils.noRippleClickable

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
            viewModel.retryPendingChanges(
                onRecoverableSecurityException = { intentSender ->
                    pendingLauncher?.invoke(IntentSenderRequest.Builder(intentSender).build())
                },
                onSuccess = {
                    Toast.makeText(context, "Cambios guardados", Toast.LENGTH_SHORT).show()
                    onBack()
                }
            )
        }
    }

    // Launcher para abrir galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onCoverChange(it)
        }
    }

    LaunchedEffect(song) {
        viewModel.loadSong(song)
    }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Arrow Back",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier.noRippleClickable(onClick = { pickImageLauncher.launch("image/*") })
        ) {
            AlbumCover(coverUri = uiState.coverUri, size = 230)
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.title,
            onValueChange = { viewModel.onTitleChange(it) },
            label = { Text(text = stringResource(R.string.title)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold,
                unfocusedBorderColor = Gold.copy(alpha = 0.5f),
                focusedLabelColor = Gold,
                unfocusedLabelColor = Gold.copy(alpha = 0.7f),
                cursorColor = Gold,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        OutlinedTextField(
            value = uiState.artist,
            onValueChange = { viewModel.onArtistChange(it) },
            label = { Text(text = stringResource(R.string.artist)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold,
                unfocusedBorderColor = Gold.copy(alpha = 0.5f),
                focusedLabelColor = Gold,
                unfocusedLabelColor = Gold.copy(alpha = 0.7f),
                cursorColor = Gold,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        OutlinedTextField(
            value = uiState.album,
            onValueChange = { viewModel.onAlbumChange(it) },
            label = { Text(text = stringResource(R.string.album)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold,
                unfocusedBorderColor = Gold.copy(alpha = 0.5f),
                focusedLabelColor = Gold,
                unfocusedLabelColor = Gold.copy(alpha = 0.7f),
                cursorColor = Gold,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.saveChanges(
                    onRecoverableSecurityException = { intentSender ->
                        launcher.launch(IntentSenderRequest.Builder(intentSender).build())
                    },
                    onSuccess = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.changes_saved),
                            Toast.LENGTH_SHORT
                        ).show()
                        onBack()
                    }
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gold,
                contentColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_changes))
        }
    }
}

