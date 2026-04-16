package com.zachstopper.tagmusicplayer.data.repository

import com.zachstopper.tagmusicplayer.data.dao.SongTagDao
import com.zachstopper.tagmusicplayer.data.entity.SongTagCrossRef
import com.zachstopper.tagmusicplayer.model.SongWithTags
import kotlinx.coroutines.flow.Flow

class SongTagRepository(
    private val dao: SongTagDao
) {

    val allSongsWithTags: Flow<List<SongWithTags>> =
        dao.getAllSongsWithTags()

    suspend fun assignTagToSong(songId: Long, tagId: Long) {
        val crossRef = SongTagCrossRef(songId, tagId)
        dao.assignTag(crossRef)
    }

    suspend fun removeTagFromSong(songId: Long, tagId: Long) {
        dao.removeTagFromSong(songId, tagId)
    }

    suspend fun isSongTagged(songId: Long, tagId: Long): Boolean {
        return dao.isSongTagged(songId, tagId)
    }

}