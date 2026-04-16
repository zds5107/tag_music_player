package com.zachstopper.tagmusicplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zachstopper.tagmusicplayer.data.repository.TagGroupRepository
import com.zachstopper.tagmusicplayer.data.repository.TagTagGroupRepository


class TagSelectionScreenViewModelFactory(
    private val tagTagGroupRepository: TagTagGroupRepository,
    private val tagGroupRepository: TagGroupRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagSelectionScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagSelectionScreenViewModel(tagTagGroupRepository, tagGroupRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}