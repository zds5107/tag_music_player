package com.zachstopper.tagmusicplayer.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "song_table",
    indices = [Index(value = ["mediaStoreId"], unique = true)]
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val artist: String,
    val album: String,
    val uri: String,
    val relativePath: String,
    val albumArtUri: String?,
    val mediaStoreId: Long
)