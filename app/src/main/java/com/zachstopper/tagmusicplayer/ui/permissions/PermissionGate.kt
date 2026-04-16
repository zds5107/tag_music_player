package com.zachstopper.tagmusicplayer.ui.permissions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun PermissionGate(
    onPermissionGranted: () -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            onPermissionGranted()
        }
    }

    Column {

        Text("This app needs permission to read audio files.")

        Button(
            onClick = {
                permissionLauncher.launch(
                    Manifest.permission.READ_MEDIA_AUDIO
                )
            }
        ) {
            Text("Grant Permission")
        }
    }
}