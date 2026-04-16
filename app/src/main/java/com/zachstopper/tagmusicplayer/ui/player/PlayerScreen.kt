package com.zachstopper.tagmusicplayer.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zachstopper.tagmusicplayer.R
import com.zachstopper.tagmusicplayer.viewmodel.PlayerScreenViewModel

@Composable
fun PlayerScreen(
    viewModel: PlayerScreenViewModel
) {

    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val playlist by viewModel.playlist.collectAsState()
    val albumArt by viewModel.currentAlbumArt.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val message by viewModel.snackbarMessage.collectAsState(initial = null)
    val isLooping by viewModel.isLooping.collectAsState()
    val isLoved by viewModel.isLoved.collectAsState()

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 92.dp,
                        bottom = 12.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(8.dp))


                val screenHeight = LocalConfiguration.current.screenHeightDp.dp

                // --- Album Art ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.33f),
                    contentAlignment = Alignment.Center
                ) {
                    if (albumArt != null) {
                        AsyncImage(
                            model = albumArt,
                            contentDescription = "Album Art",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_music_placeholder),
                            contentDescription = "Placeholder",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Text block ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentSong?.song?.title ?: "No song playing",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentSong?.song?.artist ?: "",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentSong?.song?.album ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))


                // --- Slider + Controls block ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    PlaybackSlider(viewModel)

                    PlaybackControls(
                        isPlaying = isPlaying,
                        onPrev = { viewModel.prevSong() },
                        onPlayPause = {
                            if (isPlaying) viewModel.pause() else viewModel.resume()
                        },
                        onNext = { viewModel.nextSong() }
                    )
                }
            }




            LibraryToolsPopup(
                onScanLibrary = { viewModel.scanDeviceSongs() },
                onAutoTag = { viewModel.runAutoTagging() }
            )


            FavoriteButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 88.dp, top = 16.dp),
                isLoved = isLoved,
                onClick = { viewModel.toggleLoved() }
            )

            LoopButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 88.dp, top = 16.dp),
                isLooping = isLooping,
                onClick = { viewModel.toggleLoop() }

            )


            QueueBottomSheet(
                playlist = playlist,
                currentSong = currentSong,
                onSongClick = { viewModel.playSong(it) }
            )



        }
    }
}