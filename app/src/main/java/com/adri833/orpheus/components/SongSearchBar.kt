package com.adri833.orpheus.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.res.stringResource
import com.adri833.orpheus.R

@Composable
fun SongSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                painter = rememberVectorPainter(Icons.Default.Search),
                contentDescription = "Search Icon",
                tint = Color(0xFFAAAAAA)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Close),
                    contentDescription = "Clear Icon",
                    tint = Color(0xFFAAAAAA),
                    modifier = Modifier
                        .clickable { onQueryChange("") }
                )
            }
        },
        placeholder = {
            Text(
                stringResource(R.string.search),
                color = Color(0xFF888888),
                fontSize = 14.sp
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1C1C1C),
            unfocusedContainerColor = Color(0xFF1C1C1C),
            disabledContainerColor = Color(0xFF1C1C1C),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(30.dp))
    )
}
