package com.zachstopper.tagmusicplayer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zachstopper.tagmusicplayer.data.repository.SongRepository
import com.zachstopper.tagmusicplayer.data.repository.SongTagRepository
import com.zachstopper.tagmusicplayer.data.service.AutoTagService
import com.zachstopper.tagmusicplayer.data.service.LovedTagService


class PlayerScreenViewModelFactory(
    private val context: Context,
    private val songTagRepository: SongTagRepository,
    private val songRepository: SongRepository,
    private val autoTagService: AutoTagService,
    private val lovedTagService: LovedTagService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerScreenViewModel(context.applicationContext, songTagRepository, songRepository, autoTagService, lovedTagService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}