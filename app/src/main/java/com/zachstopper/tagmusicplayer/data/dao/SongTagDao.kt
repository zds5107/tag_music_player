package com.zachstopper.tagmusicplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.zachstopper.tagmusicplayer.data.entity.SongTagCrossRef
import com.zachstopper.tagmusicplayer.model.SongWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface SongTagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRef: SongTagCrossRef)

    @Transaction
    @Query("SELECT * FROM song_table")
    fun getAllSongsWithTags(): Flow<List<SongWithTags>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun assignTag(crossRef: SongTagCrossRef)

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM song_tag_cross_ref
            WHERE songId = :songId AND tagId = :tagId
        )
    """)
    suspend fun isSongTagged(songId: Long, tagId: Long): Boolean

    @Query("""
        DELETE FROM song_tag_cross_ref
        WHERE songId = :songId AND tagId = :tagId
    """)
    suspend fun removeTagFromSong(songId: Long, tagId: Long)

}