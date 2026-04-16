package com.zachstopper.tagmusicplayer.ui.player

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isLoved: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier.size(56.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Favorite",
            modifier = Modifier.size(30.dp),
            tint = if (isLoved) {
                Color.Red
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}


