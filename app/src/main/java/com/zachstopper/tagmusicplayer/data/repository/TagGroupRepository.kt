package com.zachstopper.tagmusicplayer.data.repository

import com.zachstopper.tagmusicplayer.data.dao.TagGroupDao
import com.zachstopper.tagmusicplayer.data.entity.TagGroupEntity

class TagGroupRepository(
    private val dao: TagGroupDao
) {

    suspend fun insertTagGroup(name: String) {
        dao.insert(TagGroupEntity(name = name))
    }

}