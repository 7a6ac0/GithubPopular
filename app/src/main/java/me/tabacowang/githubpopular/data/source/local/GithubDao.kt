package me.tabacowang.githubpopular.data.source.local

import android.arch.persistence.room.*
import me.tabacowang.githubpopular.data.FavoriteRepo
import me.tabacowang.githubpopular.data.Repo

@Dao interface GithubDao {
    @Query("SELECT * FROM repos WHERE searchQuery = :searchQuery") fun getRepos(searchQuery: String): List<Repo>

    @Query("SELECT * FROM repos WHERE id = :repoId") fun getRepoById(repoId: String): Repo?

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertRepo(repo: Repo)

    @Update fun updateRepo(repo: Repo): Int

    @Query("UPDATE repos SET isFavorite = :isFavorite WHERE id = :repoId")
    fun updateFavoriteRepo(repoId: String, isFavorite: Boolean)

    @Query("DELETE FROM repos WHERE id = :repoId") fun deleteRepoById(repoId: String): Int

    @Query("DELETE FROM repos") fun deleteRepos()

    @Query("SELECT * from favorite_repos") fun getFavoriteRepos(): List<FavoriteRepo>

    @Query("SELECT * from favorite_repos WHERE repoId = :favoriteRepoId") fun getFavoriteRepoById(favoriteRepoId: String): FavoriteRepo?

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertFavoriteRepo(favoriteRepo: FavoriteRepo)

    @Query("DELETE FROM favorite_repos WHERE repoId = :favoriteRepoId") fun deleteFavoriteRepoById(favoriteRepoId: String)
}