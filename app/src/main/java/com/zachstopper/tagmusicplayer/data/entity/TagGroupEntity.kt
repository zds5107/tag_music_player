package com.zachstopper.tagmusicplayer.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "tag_group_table",
    indices = [Index(value = ["name"], unique = true)]
)
data class TagGroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)