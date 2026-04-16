package com.zachstopper.tagmusicplayer.data.entity

import androidx.room.Entity

@Entity(
    primaryKeys = ["songId", "tagId"],
    tableName = "song_tag_cross_ref"
)
data class SongTagCrossRef(
    val songId: Long,
    val tagId: Long
)