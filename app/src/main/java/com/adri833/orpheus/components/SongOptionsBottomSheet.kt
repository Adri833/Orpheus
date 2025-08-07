package com.adri833.orpheus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.domain.model.Song
import com.adri833.orpheus.R
import com.adri833.orpheus.ui.theme.Gold
import com.adri833.orpheus.ui.theme.darkGray
import com.adri833.orpheus.utils.OptionText
import com.adri833.orpheus.utils.noRippleClickable

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
        sheetState = sheetState,
        dragHandle = null,
        containerColor = darkGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(darkGray)
        ) {
            OptionText(
                text = stringResource(R.string.cancel),
                color = Gold,
                fontSize = 19f,
                modifier = Modifier
                    .align(Alignment.Start)
                    .noRippleClickable { onDismiss() }
                    .padding(18.dp),
            )

            OptionListItem(
                icon = painterResource(R.drawable.ic_add_to_queue),
                text = stringResource(R.string.add_to_queue)
            ) {
                onAddToQueue(song)
                onDismiss()
            }

            OptionListItem(
                icon = painterResource(R.drawable.ic_vinyl),
                text = stringResource(R.string.go_to_album)
            ) {
                onGoToAlbum(song)
                onDismiss()
            }

            OptionListItem(
                icon = painterResource(R.drawable.ic_artist),
                text = stringResource(R.string.go_to_artist)
            ) {
                onGoToArtist(song)
                onDismiss()
            }

            OptionListItem(
                icon = painterResource(R.drawable.ic_information),
                text = stringResource(R.string.edit_info)
            ) {
                onEditInfo(song)
                onDismiss()
            }

            OptionListItem(
                icon = painterResource(R.drawable.ic_share),
                text = stringResource(R.string.share)
            ) {
                onShare(song)
                onDismiss()
            }

            OptionListItem(
                icon = painterResource(R.drawable.ic_delete),
                text = stringResource(R.string.delete)
            ) {
                onDelete(song)
                onDismiss()
            }
        }
    }
}