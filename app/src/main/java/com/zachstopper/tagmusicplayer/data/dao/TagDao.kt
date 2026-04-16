package com.zachstopper.tagmusicplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zachstopper.tagmusicplayer.data.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: TagEntity): Long

    @Query("SELECT * FROM tag_table ORDER BY id ASC")
    fun getAllTags(): Flow<List<TagEntity>>

    @Query("SELECT id FROM tag_table WHERE name = :name LIMIT 1")
    suspend fun getTagIdByName(name: String): Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity): Long

}