package com.zachstopper.tagmusicplayer.media

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerController(
    private val context: Context,
) {

    private val player = ExoPlayer.Builder(context).build()

    private val mediaSession: MediaSession

    var onIsPlayingChanged: ((Boolean) -> Unit)? = null
    var onPositionChanged: ((Long, Long) -> Unit)? = null
    var onMediaItemChanged: ((Int) -> Unit)? = null
    var onError: ((PlaybackException) -> Unit)? = null


    private var positionJob: Job? = null


    init {

        player.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build(),
            true
        )

        mediaSession = MediaSession.Builder(context, player).build()

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                onIsPlayingChanged?.invoke(isPlaying)
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                onMediaItemChanged?.invoke(player.currentMediaItemIndex)
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    skipToNextSafe()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                onError?.invoke(error)
                Log.d("PlayerState", "error = $error")
                skipToNextSafe()
            }

        })
    }

    fun setPlaylist(uris: List<Uri>, startIndex: Int = 0) {
        val mediaItems = uris.map { MediaItem.fromUri(it) }
        player.setMediaItems(mediaItems, startIndex, 0)
        player.prepare()
        startPositionUpdates()
    }

    fun playAtIndex(index: Int) {
        player.seekTo(index, 0)
        player.play()
    }

    fun play() = player.play()
    fun pause() = player.pause()
    fun seekTo(positionMs: Long) = player.seekTo(positionMs)
    fun seekToNext() = player.seekToNext()
    fun seekToPrevious() = player.seekToPrevious()

    fun release() {
        stopPositionUpdates()
        mediaSession.release()
        player.release()
    }


    private fun skipToNextSafe() {

        while (player.hasNextMediaItem()) {

            player.seekToNext()

            val nextIndex = player.currentMediaItemIndex
            val nextUri = player.currentMediaItem?.localConfiguration?.uri

            try {
                // Force reset the player state
                player.stop()
                player.prepare()
                player.playWhenReady = true
                player.play()

                // Notify UI
                onMediaItemChanged?.invoke(nextIndex)
                onIsPlayingChanged?.invoke(player.isPlaying)

                return
            } catch (e: Exception) {
                Log.e("PlayerController", "Playback failed at index=$nextIndex, uri=$nextUri", e)
            }
        }

        player.pause()
        onIsPlayingChanged?.invoke(false)
    }

    private fun startPositionUpdates() {
        stopPositionUpdates()
        positionJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                onPositionChanged?.invoke(player.currentPosition, player.duration)
                delay(250L)
            }
        }
    }

    private fun stopPositionUpdates() {
        positionJob?.cancel()
    }

    fun setLooping(loop: Boolean) {
        player.repeatMode = if (loop) {
            Player.REPEAT_MODE_ONE
        } else {
            Player.REPEAT_MODE_OFF
        }
    }



}