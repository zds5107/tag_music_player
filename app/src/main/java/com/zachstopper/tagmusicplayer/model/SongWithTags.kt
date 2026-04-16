package com.zachstopper.tagmusicplayer.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.zachstopper.tagmusicplayer.data.entity.SongEntity
import com.zachstopper.tagmusicplayer.data.entity.SongTagCrossRef
import com.zachstopper.tagmusicplayer.data.entity.TagEntity

data class SongWithTags(
    @Embedded val song: SongEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            SongTagCrossRef::class,
            parentColumn = "songId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
)