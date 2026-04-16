package com.zachstopper.tagmusicplayer.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LibraryToolsPopup(
    onScanLibrary: () -> Unit,
    onAutoTag: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // --- Top-left button ---
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Library Tools",
                modifier = Modifier.size(30.dp)
            )
        }

        // --- Scrim ---
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { expanded = false }
            )
        }

        // --- Popup menu ---
        AnimatedVisibility(
            visible = expanded,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 80.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Library Tools",
                        style = MaterialTheme.typography.titleSmall
                    )

                    HorizontalDivider(
                        modifier = Modifier,
                        thickness = 1.dp,
                        color = Color.Gray
                    )


                    Text(
                        text = "Scan Library",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onScanLibrary()
                                expanded = false
                            }
                            .padding(8.dp)
                    )

                    Text(
                        text = "Auto Tag Songs",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAutoTag()
                                expanded = false
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}