package me.tabacowang.githubpopular.data.source

import me.tabacowang.githubpopular.data.FavoriteRepo
import me.tabacowang.githubpopular.data.Repo

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

    fun getRepos(searchQuery: String, callback: LoadReposCallback)

    fun getRepo(repoId: String, callback: GetRepoCallback)

    fun saveRepo(repo: Repo)

    fun updateFavoriteRepo(repoId: String, isFavorite: Boolean)

    fun refreshRepos()

    fun deleteAllRepos()

    fun deleteRepo(repoId: String)

    fun getFavoriteRepos(callback: LoadFavoriteReposCallback)

    fun saveFavoriteRepo(repoId: String)

    fun deleteFavoriteRepo(repoId: String)
}