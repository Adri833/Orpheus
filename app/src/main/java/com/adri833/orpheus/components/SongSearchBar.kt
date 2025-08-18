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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.adri833.orpheus.R
import com.adri833.orpheus.ui.theme.LightGray
import com.adri833.orpheus.ui.theme.MediumGray
import com.adri833.orpheus.ui.theme.NearBlack

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
                tint = LightGray
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Close),
                    contentDescription = "Clear Icon",
                    tint = LightGray,
                    modifier = Modifier
                        .clickable { onQueryChange("") }
                )
            }
        },
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                color = MediumGray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = NearBlack,
            unfocusedContainerColor = NearBlack,
            disabledContainerColor = NearBlack,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.White
        ),
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .padding(horizontal = 2.dp)
            .shadow(4.dp, RoundedCornerShape(30.dp))
    )
}