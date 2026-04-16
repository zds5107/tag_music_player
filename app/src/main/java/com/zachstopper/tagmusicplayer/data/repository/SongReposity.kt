package com.zachstopper.tagmusicplayer.data.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.zachstopper.tagmusicplayer.data.dao.SongDao
import com.zachstopper.tagmusicplayer.data.entity.SongEntity


class SongRepository(
    private val dao: SongDao
) {

    suspend fun upsertAll(songs: List<SongEntity>) {

        val existingSongs = dao.getAllSongs()
            .associateBy { it.mediaStoreId }

        val songsToInsert = mutableListOf<SongEntity>()
        val songsToUpdate = mutableListOf<SongEntity>()

        for (song in songs) {
            val existing = existingSongs[song.mediaStoreId]


            if (existing == null) {
                songsToInsert.add(song)
            } else {
                songsToUpdate.add(song.copy(id = existing.id))
            }
        }

        dao.upsertSongsInternal(songsToInsert, songsToUpdate)
    }

    suspend fun removeMissingSongs(currentIds: List<Long>) {
        dao.deleteMissingSongs(currentIds)
    }

    suspend fun isEmpty(): Boolean = dao.getCount() == 0

    suspend fun scanDeviceSongs(context: Context) {

        val songs = mutableListOf<SongEntity>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.RELATIVE_PATH,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )?.use { cursor ->

            val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val relativePathIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.RELATIVE_PATH)
            val albumIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (cursor.moveToNext()) {

                val id = cursor.getLong(idIndex)
                val title = cursor.getString(titleIndex) ?: "Unknown Title"
                val artist = cursor.getString(artistIndex) ?: "Unknown Artist"
                val album = cursor.getString(albumIndex) ?: "Unknown Album"
                val relativePath = cursor.getString(relativePathIndex) ?: ""
                val albumId = cursor.getLong(albumIdIndex)

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val albumArtUri = "content://media/external/audio/albumart/$albumId"



                songs.add(
                    SongEntity(
                        mediaStoreId = id,
                        title = title,
                        artist = artist,
                        album = album,
                        uri = contentUri.toString(),
                        relativePath = relativePath,
                        albumArtUri = albumArtUri
                    )
                )
            }
        }


        upsertAll(songs)
        removeMissingSongs(songs.map { it.mediaStoreId })

    }
}