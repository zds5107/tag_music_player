package com.zachstopper.tagmusicplayer.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumArtLoader(
    private val context: Context
) {

    suspend fun loadAlbumArt(
        albumArtUri: String?
    ): Bitmap? = withContext(Dispatchers.IO) {

        albumArtUri?.let { uri ->
            loadFromUri(uri)

        }
    }

    private fun loadFromUri(uriString: String): Bitmap? {
        return try {
            val uri = uriString.toUri()
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            }
        } catch (e: Exception) {
            Log.e("AlbumArtLoader", "Failed to load from URI", e)
            null
        }
    }
}