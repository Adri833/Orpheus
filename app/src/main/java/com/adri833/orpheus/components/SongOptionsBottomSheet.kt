package com.adri833.orpheus.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.adri833.orpheus.domain.model.Song
import com.adri833.orpheus.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongOptionsBottomSheet(
    song: Song,
    onDismiss: () -> Unit,
    onAddToQueue: (Song) -> Unit,
    onGoToAlbum: (Song) -> Unit,
    onGoToArtist: (Song) -> Unit,
    onEditInfo: (Song) -> Unit,
    onShare: (Song) -> Unit,
    onDelete: (Song) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column {
            OptionListItem(
                icon = painterResource( R.drawable.ic_add_to_queue),
                text = stringResource(R.string.add_to_queue)
            ) {
                onAddToQueue(song)
                onDismiss()
            }

            OptionListItem(
                icon = painterResource( R.drawable.ic_vinyl),
                text = stringResource(R.string.go_to_album)
            ) {
                onGoToAlbum(song)
                onDismiss()
            }

            OptionListItem(
                icon =  painterResource( R.drawable.ic_artist),
                text = stringResource(R.string.go_to_artist)
            ) {
                onGoToArtist(song)
                onDismiss()
            }

            OptionListItem(
                icon = painterResource( R.drawable.ic_information),
                text = stringResource(R.string.edit_info)
            ) {
                onEditInfo(song)
                onDismiss()
            }

            OptionListItem(
                icon = painterResource( R.drawable.ic_share),
                text = stringResource(R.string.share)
            ) {
                onShare(song)
                onDismiss()
            }

            OptionListItem(
                icon = painterResource( R.drawable.ic_delete),
                text = stringResource(R.string.delete),
            ) {
                onDelete(song)
                onDismiss()
            }
        }
    }
}