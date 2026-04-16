package com.zachstopper.tagmusicplayer.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zachstopper.tagmusicplayer.data.repository.SongRepository
import com.zachstopper.tagmusicplayer.data.repository.SongTagRepository
import com.zachstopper.tagmusicplayer.data.service.AutoTagService
import com.zachstopper.tagmusicplayer.data.service.LovedTagService
import com.zachstopper.tagmusicplayer.media.AlbumArtLoader
import com.zachstopper.tagmusicplayer.media.PlayerController
import com.zachstopper.tagmusicplayer.model.SongWithTags
import com.zachstopper.tagmusicplayer.model.TagFilterRules
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlayerScreenViewModel(
    private val appContext: Context,
    private val songTagRepository: SongTagRepository,
    private val songRepository: SongRepository,
    private val autoTagService: AutoTagService,
    private val lovedTagService: LovedTagService
) : ViewModel() {

    // Media / Playback
    private val albumArtLoader = AlbumArtLoader(appContext)
    private val controller = PlayerController(appContext)

    // Playback State
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentSong = MutableStateFlow<SongWithTags?>(null)
    val currentSong: StateFlow<SongWithTags?> = _currentSong

    private val _playlist = MutableStateFlow<List<SongWithTags>>(emptyList())
    val playlist: StateFlow<List<SongWithTags>> = _playlist

    // Position
    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    private val _currentDuration = MutableStateFlow(0)
    val currentDuration: StateFlow<Int> = _currentDuration

    // UI Extras

    private val _currentAlbumArt = MutableStateFlow<Bitmap?>(null)
    val currentAlbumArt: StateFlow<Bitmap?> = _currentAlbumArt

    private val _isLooping = MutableStateFlow(false)
    val isLooping:  StateFlow<Boolean> = _isLooping

    private val _isLoved = MutableStateFlow(false)
    val isLoved: StateFlow<Boolean> = _isLoved

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage


    init {
        setupControllerCallbacks()
        initializeLibrary()
    }

    // Playback Controls
    fun playSong(songWithTags: SongWithTags) {
        val index = _playlist.value.indexOf(songWithTags)
        if (index != -1) controller.playAtIndex(index)
    }
    fun nextSong() = controller.seekToNext()
    fun prevSong() = controller.seekToPrevious()
    fun pause() = controller.pause()
    fun resume() = controller.play()
    fun seekTo(position: Int) = controller.seekTo(position.toLong())

    // Playlist
    fun generatePlaylist(rules: TagFilterRules) {
        viewModelScope.launch {

            val allSongsWithTags = songTagRepository.allSongsWithTags.first()
            val filtered = filterSongs(allSongsWithTags, rules)

            setPlaylist(filtered.shuffled())

        }
    }
    fun setPlaylist(songs: List<SongWithTags>) {
        _playlist.value = songs

        if (songs.isEmpty()) return

        val uris = songs.map { it.song.uri.toUri() }
        controller.setPlaylist(uris)
    }

    // Library Actions
    fun scanDeviceSongs() {
        viewModelScope.launch {
            showSnackbar("Scanning library…")
            songRepository.scanDeviceSongs(appContext)
            showSnackbar("Scan complete")
        }
    }
    fun runAutoTagging() {
        viewModelScope.launch {
            showSnackbar("Auto tagging songs…")

            autoTagService.runAutoTagging()

            showSnackbar("Auto tagging complete")
        }
    }

    // UI Actions
    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
    fun toggleLoop() {
        val newValue = !_isLooping.value
        _isLooping.value = newValue
        controller.setLooping(newValue)
    }
    fun toggleLoved() {
        val songId = _currentSong.value?.song?.id ?: return

        viewModelScope.launch {
            lovedTagService.toggleLoved(songId)

            _isLoved.value = !_isLoved.value
        }
    }

    private fun filterSongs(
        allSongsWithTags: List<SongWithTags>,
        rules: TagFilterRules
    ): List<SongWithTags> {

        return allSongsWithTags.filter { songWithTags ->
            val tagIds = songWithTags.tags.map { it.id }.toSet()

            if (tagIds.any { it in rules.exclude }) return@filter false
            if (!rules.includeTier1.all { it in tagIds }) return@filter false

            if (rules.includeTier2.isNotEmpty()) {
                if (tagIds.none { it in rules.includeTier2 }) return@filter false
            }

            true
        }
    }

    private fun loadCurrentAlbumArt(song: SongWithTags) {
        viewModelScope.launch {

            val bitmap = albumArtLoader.loadAlbumArt(
                albumArtUri = song.song.albumArtUri
            )
            _currentAlbumArt.value = bitmap
        }
    }

    private fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    private fun setupControllerCallbacks() {

        controller.onIsPlayingChanged = { _isPlaying.value = it }

        controller.onMediaItemChanged = { index ->
            val song = _playlist.value.getOrNull(index)
            _currentSong.value = song

            song?.let {
                loadCurrentAlbumArt(it)

                viewModelScope.launch {
                    val loved = lovedTagService.isSongLoved(it.song.id)
                    _isLoved.value = loved
                }
            }
        }

        controller.onPositionChanged = { pos, dur ->
            _currentPosition.value = pos.toInt()
            _currentDuration.value = dur.toInt()
        }

        controller.onError = { error ->
            Log.e("PlayerVM", "Skipped track due to error: ${error.message}")
        }
    }

    private fun initializeLibrary() {
        viewModelScope.launch {
            if (songRepository.isEmpty()) {
                scanDeviceSongs()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        controller.release()
    }

}

