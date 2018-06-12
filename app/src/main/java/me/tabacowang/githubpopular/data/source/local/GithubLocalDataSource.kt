package me.tabacowang.githubpopular.data.source.local

import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.source.GithubDataSource
import me.tabacowang.githubpopular.util.AppExecutors

class GithubLocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val popularDao: PopularDao
) : GithubDataSource {

    companion object {
        private var INSTANCE: GithubLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, popularDao: PopularDao): GithubLocalDataSource =
                INSTANCE ?: synchronized(GithubLocalDataSource::class.java) {
                    INSTANCE ?: GithubLocalDataSource(appExecutors, popularDao)
                            .also { INSTANCE = it }
                }
    }

    override fun getRepos(searchQuery: String, callback: GithubDataSource.LoadReposCallback) {
        appExecutors.diskIO.execute {
            val repos = popularDao.getRepos(searchQuery).sortedByDescending { it.stars }
            appExecutors.mainThread.execute {
                if (repos.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onReposLoaded(repos)
                }
            }

        }
    }

    override fun getRepo(repoId: String, callback: GithubDataSource.GetRepoCallback) {
        appExecutors.diskIO.execute {
            val repo = popularDao.getRepoById(repoId)
            appExecutors.mainThread.execute {
                if (repo != null) {
                    callback.onRepoLoaded(repo)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveRepo(repo: Repo) {
        appExecutors.diskIO.execute { popularDao.insertRepo(repo) }
    }

    override fun refreshRepos() {
    }

    override fun deleteAllRepos() {
        appExecutors.diskIO.execute { popularDao.deleteRepos() }
    }

    override fun deleteRepo(repoId: String) {
        appExecutors.diskIO.execute { popularDao.deleteRepoById(repoId) }
    }
}