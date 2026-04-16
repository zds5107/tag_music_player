package com.zachstopper.tagmusicplayer.data.service

import com.zachstopper.tagmusicplayer.data.repository.SongTagRepository
import com.zachstopper.tagmusicplayer.data.repository.TagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AutoTagService(
    private val tagRepository: TagRepository,
    private val songTagRepository: SongTagRepository
) {

    suspend fun runAutoTagging() = withContext(Dispatchers.IO) {

        val songsWithTags = songTagRepository.allSongsWithTags.first()

        val tagCache = mutableMapOf<String, Long>()

        for (songWithTags in songsWithTags) {

            if (songWithTags.song.relativePath.isBlank()) continue

            val folders = songWithTags.song.relativePath
                .trimEnd('/')
                .split('/')
                .filter {it != "Music"}

            for (folder in folders) {

                val tagId = tagCache.getOrPut(folder) {
                    tagRepository.createTagWithDefaultGroup(folder)
                }

                songTagRepository.assignTagToSong(songWithTags.song.id, tagId)

            }

        }
    }
}