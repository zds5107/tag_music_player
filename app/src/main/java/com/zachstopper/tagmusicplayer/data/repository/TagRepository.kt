package com.zachstopper.tagmusicplayer.data.repository

import com.zachstopper.tagmusicplayer.data.dao.TagDao
import com.zachstopper.tagmusicplayer.data.entity.TagEntity

class TagRepository(
    private val dao: TagDao,
    private val tagTagGroupRepository: TagTagGroupRepository
) {

    suspend fun createTagWithDefaultGroup(tagName: String): Long {
        val existingId = dao.getTagIdByName(tagName)
        if (existingId != null) return existingId

        val newTagId = dao.insert(TagEntity(name = tagName))

        tagTagGroupRepository.assignToDefaultGroup(newTagId)

        return newTagId

    }

}