package com.zachstopper.tagmusicplayer.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.zachstopper.tagmusicplayer.data.entity.TagEntity
import com.zachstopper.tagmusicplayer.data.entity.TagGroupEntity
import com.zachstopper.tagmusicplayer.data.entity.TagTagGroupCrossRef

data class TagGroupWithTags(
    @Embedded val tagGroup: TagGroupEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            TagTagGroupCrossRef::class,
            parentColumn = "tagGroupId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
)