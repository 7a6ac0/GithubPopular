package me.tabacowang.githubpopular.data.source.local

import android.arch.persistence.room.*
import me.tabacowang.githubpopular.data.FavoriteRepo
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.RepoSearchResult

@Dao interface GithubDao {
    // Repo
    @Query("SELECT * FROM repos WHERE searchQuery = :searchQuery") fun getRepos(searchQuery: String): List<Repo>

    @Query("SELECT * FROM repos WHERE id = :repoId") fun getRepoById(repoId: String): Repo?

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertRepo(repo: Repo)

    @Update fun updateRepo(repo: Repo): Int

    @Query("UPDATE repos SET isFavorite = :isFavorite WHERE id = :repoId")
    fun updateFavoriteRepo(repoId: String, isFavorite: Boolean)

    @Query("DELETE FROM repos WHERE searchQuery = :searchQuery") fun deleteRepoByQuery(searchQuery: String): Int

    @Query("DELETE FROM repos") fun deleteRepos()

    // FavoriteRepo
    @Query("SELECT * from favorite_repos") fun getFavoriteRepos(): List<FavoriteRepo>

    @Query("SELECT * from favorite_repos WHERE repoId = :favoriteRepoId") fun getFavoriteRepoById(favoriteRepoId: String): FavoriteRepo?

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertFavoriteRepo(favoriteRepo: FavoriteRepo)

    @Query("DELETE FROM favorite_repos WHERE repoId = :favoriteRepoId") fun deleteFavoriteRepoById(favoriteRepoId: String): Int

    // RepoSearchResult
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertSearchQuery(repoSearchResult: RepoSearchResult)

    @Query("SELECT * FROM repo_search_result WHERE searchQuery = :searchQuery") fun getSearchResult(searchQuery: String): RepoSearchResult?

    @Query("DELETE FROM repo_search_result WHERE searchQuery = :searchQuery") fun deleteSearchResultByQuery(searchQuery: String): Int
}