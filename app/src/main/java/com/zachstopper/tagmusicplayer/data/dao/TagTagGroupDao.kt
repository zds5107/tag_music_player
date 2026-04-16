package com.zachstopper.tagmusicplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.zachstopper.tagmusicplayer.data.entity.TagTagGroupCrossRef
import com.zachstopper.tagmusicplayer.model.TagGroupWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface TagTagGroupDao {

    @Transaction
    @Query("SELECT * FROM tag_group_table")
    fun allTagGroupsWithTags(): Flow<List<TagGroupWithTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: TagTagGroupCrossRef)

    @Transaction
    suspend fun assignTagsToGroup(groupId:Long, tagIds: Set<Long>) {
        tagIds.forEach { tagId ->
            insert(TagTagGroupCrossRef(tagId, groupId))

        }
    }



}