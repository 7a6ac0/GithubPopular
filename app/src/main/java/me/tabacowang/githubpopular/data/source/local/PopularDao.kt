package me.tabacowang.githubpopular.data.source.local

import android.arch.persistence.room.*
import me.tabacowang.githubpopular.data.Repo

@Dao interface PopularDao {
    @Query("SELECT * FROM repos WHERE searchQuery = :searchQuery") fun getRepos(searchQuery: String): List<Repo>

    @Query("SELECT * FROM repos WHERE id = :repoId") fun getRepoById(repoId: String): Repo?

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertRepo(repo: Repo)

    @Update fun updateRepo(repo: Repo): Int

    @Query("DELETE FROM repos WHERE id = :repoId") fun deleteRepoById(repoId: String): Int

    @Query("DELETE FROM repos") fun deleteRepos()
}