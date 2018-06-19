package me.tabacowang.githubpopular.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import me.tabacowang.githubpopular.data.FavoriteRepo
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.RepoSearchResult

@Database(entities = [Repo::class, FavoriteRepo::class, RepoSearchResult::class], version = 1)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun githubDao(): GithubDao

    companion object {
        private var INSTANCE: GithubDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): GithubDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            GithubDatabase::class.java, "Repos.db")
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}