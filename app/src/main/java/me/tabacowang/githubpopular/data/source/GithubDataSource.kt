package me.tabacowang.githubpopular.data.source

import me.tabacowang.githubpopular.data.FavoriteRepo
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.RepoSearchResult

interface GithubDataSource {
    interface LoadReposCallback {

        fun onReposLoaded(repos: List<Repo>)

        fun onDataNotAvailable()
    }

    interface LoadTrendReposCallback {

        fun onTrendReposLoaded(repos: List<Repo>)

        fun onDataNotAvailable()
    }

    interface GetRepoCallback {

        fun onRepoLoaded(repo: Repo)

        fun onDataNotAvailable()
    }

    interface LoadFavoriteReposCallback {

        fun onFavoriteReposLoaded(favoriteRepos: List<FavoriteRepo>)

        fun onDataNotAvailable()
    }

    fun getTrendRepos(callback: LoadTrendReposCallback)

    fun getRepos(searchQuery: String, page: Int, callback: LoadReposCallback)

    fun getRepo(repoId: String, callback: GetRepoCallback)

    fun saveRepo(repo: Repo)

    fun refreshRepos()

    fun deleteAllRepos()

    fun deleteRepo(searchQuery: String)

    fun updateFavoriteRepo(favoriteRepo: Repo, isFavorite: Boolean)

    fun getFavoriteRepos(callback: LoadFavoriteReposCallback)

    fun saveFavoriteRepo(favoriteRepo: Repo)

    fun deleteFavoriteRepo(favoriteRepo: Repo)

    interface GetSearchResultCallback {

        fun onResultLoaded(repoSearchResult: RepoSearchResult)

        fun onDataNotAvailable()
    }

    fun saveSearchResult(repoSearchResult: RepoSearchResult)

    fun getSearchResult(searchQuery: String, callback: GetSearchResultCallback)

    fun deleteSearchResult(searchQuery: String)
}