package com.adri833.orpheus.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.adri833.orpheus.R
import android.content.Intent
import android.provider.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.adri833.orpheus.ui.theme.LightGray
import com.adri833.orpheus.utils.adjustForMobile

@Composable
fun DeniedPermissionUI() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .adjustForMobile()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_permission_audio),
            contentDescription = "Permiso requerido",
            modifier = Modifier.size(120.dp),
            colorFilter = ColorFilter.tint(LightGray)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.permission_audio_description))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.permission_audio_path))
                }
            },
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.merriweather_regular)),
            lineHeight = 28.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:${context.packageName}".toUri()
            }
            context.startActivity(intent)
        }) {
            Text("Abrir ajustes")
        }
    }
}