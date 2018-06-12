package me.tabacowang.githubpopular.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import me.tabacowang.githubpopular.data.Repo

@Database(entities = [Repo::class], version = 1)
abstract class PopularDatabase : RoomDatabase() {
    abstract fun popularDao(): PopularDao

    companion object {
        private var INSTANCE: PopularDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): PopularDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            PopularDatabase::class.java, "Repos.db")
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}