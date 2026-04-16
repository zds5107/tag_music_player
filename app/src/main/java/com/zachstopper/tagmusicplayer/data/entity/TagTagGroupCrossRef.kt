package com.zachstopper.tagmusicplayer.data.entity

import androidx.room.Entity

@Entity(
    primaryKeys = ["tagId"],
    tableName = "tag_tag_group_cross_ref"
)
data class TagTagGroupCrossRef(
    val tagId: Long,
    val tagGroupId: Long
)