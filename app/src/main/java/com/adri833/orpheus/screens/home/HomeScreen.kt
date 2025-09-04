package com.adri833.orpheus.screens.home

import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import coil.compose.rememberAsyncImagePainter
import com.adri833.orpheus.R
import com.adri833.orpheus.components.SelectableButton
import com.adri833.orpheus.domain.handler.AudioPermissionHandler
import com.adri833.orpheus.screens.home.contents.AlbumsHost
import com.adri833.orpheus.screens.home.contents.ArtistsHost
import com.adri833.orpheus.screens.home.contents.FoldersContent
import com.adri833.orpheus.screens.home.contents.SongsContent
import com.adri833.orpheus.screens.player.PlayerViewModel
import com.adri833.orpheus.ui.ALPHA_VISIBLE
import com.adri833.orpheus.ui.transitionFadeNormal

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(UnstableApi::class)
@Composable
fun HomeScreen(
    playerViewModel: PlayerViewModel,
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigationToInformation: () -> Unit,
) {
    val options = listOf(
        stringResource(R.string.canciones),
        stringResource(R.string.albumes),
        stringResource(R.string.artistas),
        stringResource(R.string.carpetas)
    )
    var selected by remember { mutableStateOf(options[0]) }
    val alphaAnim = remember { Animatable(0f) }
    val songs by homeViewModel.songs.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(
            targetValue = ALPHA_VISIBLE,
            animationSpec = transitionFadeNormal
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .alpha(alphaAnim.value),
        verticalArrangement = Arrangement.Top,
    ) {
        // Greeting + image
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = homeViewModel.getGreetingMessage(),
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.merriweather_bold)),
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clickable { homeViewModel.logout() }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(homeViewModel.getProfilePicture()),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }
        }

        // Buttons row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        ) {
            items(options.size) { name ->
                SelectableButton(
                    name = options[name],
                    isSelected = name == options.indexOf(selected),
                    onClick = { selected = options[name] }
                )
            }
        }

        // Main content
        AudioPermissionHandler {
            when (selected) {
                stringResource(R.string.canciones) -> SongsContent(songs, homeViewModel, playerViewModel, navigationToInformation = navigationToInformation)
                stringResource(R.string.albumes) -> AlbumsHost(songs, homeViewModel, playerViewModel)
                stringResource(R.string.artistas) -> ArtistsHost(songs, homeViewModel, playerViewModel)
                stringResource(R.string.carpetas) -> FoldersContent(homeViewModel, playerViewModel)
            }
        }
    }
}