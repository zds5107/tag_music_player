package com.zachstopper.tagmusicplayer.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zachstopper.tagmusicplayer.data.dao.SongDao
import com.zachstopper.tagmusicplayer.data.dao.SongTagDao
import com.zachstopper.tagmusicplayer.data.dao.TagDao
import com.zachstopper.tagmusicplayer.data.dao.TagGroupDao
import com.zachstopper.tagmusicplayer.data.dao.TagTagGroupDao
import com.zachstopper.tagmusicplayer.data.entity.SongEntity
import com.zachstopper.tagmusicplayer.data.entity.SongTagCrossRef
import com.zachstopper.tagmusicplayer.data.entity.TagEntity
import com.zachstopper.tagmusicplayer.data.entity.TagGroupEntity
import com.zachstopper.tagmusicplayer.data.entity.TagTagGroupCrossRef

@Database(entities = [SongEntity::class, TagEntity::class, SongTagCrossRef::class, TagGroupEntity::class, TagTagGroupCrossRef::class], version = 10, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    abstract fun tagDao(): TagDao

    abstract fun tagGroupDao(): TagGroupDao

    abstract fun songTagDao(): SongTagDao

    abstract fun tagTagGroupDao(): TagTagGroupDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "player_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}