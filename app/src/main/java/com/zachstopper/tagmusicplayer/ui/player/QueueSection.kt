package com.zachstopper.tagmusicplayer.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zachstopper.tagmusicplayer.model.SongWithTags

@Composable
fun QueueBottomSheet(
    playlist: List<SongWithTags>,
    currentSong: SongWithTags?,
    onSongClick: (SongWithTags) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Auto-scroll to current song
    LaunchedEffect(currentSong, playlist) {
        val index = playlist.indexOfFirst { it.song.id == currentSong?.song?.id }
        if (index >= 0) {
            listState.animateScrollToItem(index)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // --- Hamburger Button ---
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Queue",
                modifier = Modifier.size(32.dp)
            )
        }

        // --- Scrim (full screen overlay) ---
        if (expanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { expanded = false }
            )
        }

        // --- Bottom Sheet ---
        AnimatedVisibility(
            visible = expanded,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {

                Text(
                    text = "Up Next",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(playlist) { _, song ->

                        val isCurrent =
                            song.song.id == currentSong?.song?.id

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSongClick(song)
                                    expanded = false
                                }
                                .padding(12.dp)
                        ) {
                            Column {

                                Text(
                                    text = song.song.title,
                                    fontWeight = if (isCurrent)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal,
                                    color = if (isCurrent)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = song.song.artist,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}