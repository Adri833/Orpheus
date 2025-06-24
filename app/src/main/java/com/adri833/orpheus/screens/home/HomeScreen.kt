package com.adri833.orpheus.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.adri833.orpheus.R
import com.adri833.orpheus.components.SelectableButton
import com.adri833.orpheus.components.adjustForMobile

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val options = listOf(
        stringResource(R.string.canciones),
        stringResource(R.string.albumes),
        stringResource(R.string.artistas),
        stringResource(R.string.carpetas)
    )
    var selected by remember { mutableStateOf(options[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .adjustForMobile(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top part: greeting + image
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = viewModel.getGreetingMessage(),
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.merriweather_bold)),
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = rememberAsyncImagePainter(viewModel.getProfilePicture()),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }

        // Button row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(options.size) { name ->
                SelectableButton(
                    name = options[name],
                    isSelected = name == options.indexOf(selected),
                    onClick = { selected = options[name] }
                )
            }
        }
    }
}