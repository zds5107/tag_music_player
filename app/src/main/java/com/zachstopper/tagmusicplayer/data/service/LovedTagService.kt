package com.zachstopper.tagmusicplayer.data.service

import com.zachstopper.tagmusicplayer.data.repository.SongTagRepository
import com.zachstopper.tagmusicplayer.data.repository.TagRepository


class LovedTagService(
    private val tagRepository: TagRepository,
    private val songTagRepository: SongTagRepository
) {

    private suspend fun getLovedTagId(): Long {
        return tagRepository.createTagWithDefaultGroup("Loved")
    }

    suspend fun toggleLoved(songId: Long) {
        val tagId = getLovedTagId()

        val isLoved = songTagRepository.isSongTagged(songId, tagId)

        if (isLoved) {
            songTagRepository.removeTagFromSong(songId, tagId)
        } else {
            songTagRepository.assignTagToSong(songId, tagId)
        }
    }

    suspend fun isSongLoved(songId: Long): Boolean {
        val tagId = getLovedTagId()
        return songTagRepository.isSongTagged(songId, tagId)
    }
}