package com.zachstopper.tagmusicplayer.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zachstopper.tagmusicplayer.viewmodel.PlayerScreenViewModel


@Composable
fun PlaybackSlider(viewModel: PlayerScreenViewModel) {

    val position by viewModel.currentPosition.collectAsState()
    val duration by viewModel.currentDuration.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Slider
        Slider(
            value = if (duration == 0) 0f else position.toFloat() / duration.toFloat(),
            onValueChange = { fraction ->
                val newPosition = (fraction * duration).toInt()
                viewModel.seekTo(newPosition)
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = formatTime(position),
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = formatTime(duration),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}




// --- Helper ---
private fun formatTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}