package com.zachstopper.tagmusicplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zachstopper.tagmusicplayer.data.entity.TagGroupEntity

@Dao
interface TagGroupDao {

    @Query("SELECT id FROM tag_group_table WHERE name = :name LIMIT 1")
    suspend fun getIdByName(name: String): Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tagGroup: TagGroupEntity): Long


}