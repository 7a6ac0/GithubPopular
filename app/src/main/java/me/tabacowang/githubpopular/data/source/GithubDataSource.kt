package me.tabacowang.githubpopular.data.source

import me.tabacowang.githubpopular.data.FavoriteRepo
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.RepoSearchResult

interface GithubDataSource {
    interface LoadReposCallback {

        fun onReposLoaded(repos: List<Repo>)

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

    fun getRepos(searchQuery: String, page: Int, callback: LoadReposCallback)

    fun getRepo(repoId: String, callback: GetRepoCallback)

    fun saveRepo(repo: Repo)

    fun updateFavoriteRepo(repoId: String, isFavorite: Boolean)

    fun refreshRepos()

    fun deleteAllRepos()

    fun deleteRepo(searchQuery: String)

    fun getFavoriteRepos(callback: LoadFavoriteReposCallback)

    fun saveFavoriteRepo(repoId: String)

    fun deleteFavoriteRepo(repoId: String)

    interface GetSearchResultCallback {

        fun onResultLoaded(repoSearchResult: RepoSearchResult)

        fun onDataNotAvailable()
    }

    fun saveSearchResult(repoSearchResult: RepoSearchResult)

    fun getSearchResult(searchQuery: String, callback: GetSearchResultCallback)

    fun deleteSearchResult(searchQuery: String)
}