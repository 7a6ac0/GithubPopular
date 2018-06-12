package me.tabacowang.githubpopular.data.source

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

    fun getRepos(searchQuery: String, callback: LoadReposCallback)

    fun getRepo(repoId: String, callback: GetRepoCallback)

    fun saveRepo(repo: Repo)

    fun refreshRepos()

    fun deleteAllRepos()

    fun deleteRepo(repoId: String)
}