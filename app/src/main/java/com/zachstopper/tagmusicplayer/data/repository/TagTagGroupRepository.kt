package com.zachstopper.tagmusicplayer.data.repository

import androidx.room.withTransaction
import com.zachstopper.tagmusicplayer.data.dao.TagGroupDao
import com.zachstopper.tagmusicplayer.data.dao.TagTagGroupDao
import com.zachstopper.tagmusicplayer.data.database.AppDatabase
import com.zachstopper.tagmusicplayer.data.entity.TagGroupEntity
import com.zachstopper.tagmusicplayer.data.entity.TagTagGroupCrossRef
import com.zachstopper.tagmusicplayer.model.TagGroupWithTags
import kotlinx.coroutines.flow.Flow

class TagTagGroupRepository(
    private val tagGroupDao: TagGroupDao,
    private val dao: TagTagGroupDao,
    private val database: AppDatabase
) {

    val allTagGroupsWithTags: Flow<List<TagGroupWithTags>> =
        dao.allTagGroupsWithTags()


    suspend  fun assignToDefaultGroup(tagId: Long) {
        database.withTransaction {
            val defaultGroupId = tagGroupDao.getIdByName("Uncategorized")
                ?: tagGroupDao.insert(TagGroupEntity(name = "Uncategorized"))

            dao.insert(TagTagGroupCrossRef(tagId = tagId, tagGroupId = defaultGroupId))
        }

    }

    suspend fun assignTagsToGroup(groupId: Long, tagIds: Set<Long>) {
        dao.assignTagsToGroup(groupId, tagIds)
    }

}