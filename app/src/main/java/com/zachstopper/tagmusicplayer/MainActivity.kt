package com.zachstopper.tagmusicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.zachstopper.tagmusicplayer.data.database.AppDatabase
import com.zachstopper.tagmusicplayer.data.repository.SongRepository
import com.zachstopper.tagmusicplayer.data.repository.SongTagRepository
import com.zachstopper.tagmusicplayer.data.repository.TagGroupRepository
import com.zachstopper.tagmusicplayer.data.repository.TagRepository
import com.zachstopper.tagmusicplayer.data.repository.TagTagGroupRepository
import com.zachstopper.tagmusicplayer.ui.navigation.AppNavHost
import com.zachstopper.tagmusicplayer.ui.permissions.PermissionGate
import com.zachstopper.tagmusicplayer.ui.theme.TagMusicPlayerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(applicationContext)
        val songRepository = SongRepository(database.songDao())

        val tagTagGroupRepository = TagTagGroupRepository(
            database.tagGroupDao(),
            database.tagTagGroupDao(),
            database
        )


        val tagRepository = TagRepository(
            database.tagDao(),
            tagTagGroupRepository
            )
        val songTagRepository = SongTagRepository(database.songTagDao())

        val tagGroupRepository = TagGroupRepository(database.tagGroupDao())

        setContent {
            TagMusicPlayerTheme {

                val context = LocalContext.current

                var permissionGranted by remember {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_MEDIA_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                }

                if (permissionGranted) {
                    AppNavHost(songRepository, tagRepository, songTagRepository, tagTagGroupRepository, tagGroupRepository)
                } else {
                    PermissionGate {
                        permissionGranted = true
                    }

                }

            }
        }
    }

}





