package com.zachstopper.tagmusicplayer.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.zachstopper.tagmusicplayer.data.entity.SongEntity

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongs(songs: List<SongEntity>)

    @Query("SELECT COUNT(*) FROM song_table")
    suspend fun getCount(): Int

    @Update
    suspend fun updateSongs(songs: List<SongEntity>)

    @Query("SELECT * FROM song_table")
    suspend fun getAllSongs(): List<SongEntity>

    @Transaction
    suspend fun upsertSongsInternal(
        songsToInsert: List<SongEntity>,
        songsToUpdate: List<SongEntity>
    ) {
        if (songsToInsert.isNotEmpty()) insertSongs(songsToInsert)
        if (songsToUpdate.isNotEmpty()) updateSongs(songsToUpdate)
    }

    @Query("DELETE FROM song_table WHERE mediaStoreId NOT IN (:ids)")
    suspend fun deleteMissingSongs(ids: List<Long>)


}