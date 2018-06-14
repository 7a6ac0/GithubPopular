package me.tabacowang.githubpopular.data.source.local

import me.tabacowang.githubpopular.data.FavoriteRepo
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.source.GithubDataSource
import me.tabacowang.githubpopular.util.AppExecutors

class GithubLocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val githubDao: GithubDao
) : GithubDataSource {

    companion object {
        private var INSTANCE: GithubLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors,
                        githubDao: GithubDao): GithubLocalDataSource =
                INSTANCE ?: synchronized(GithubLocalDataSource::class.java) {
                    INSTANCE ?: GithubLocalDataSource(appExecutors, githubDao)
                            .also { INSTANCE = it }
                }
    }

    override fun getRepos(searchQuery: String, callback: GithubDataSource.LoadReposCallback) {
        appExecutors.diskIO.execute {
            val repos = githubDao.getRepos(searchQuery).sortedByDescending { it.stars }
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
            val repo = githubDao.getRepoById(repoId)
            appExecutors.mainThread.execute {
                if (repo != null) {
                    callback.onRepoLoaded(repo)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun getFavoriteRepos(callback: GithubDataSource.LoadFavoriteReposCallback) {
        appExecutors.diskIO.execute {
            val favoriteRepo = githubDao.getFavoriteRepos()
            appExecutors.mainThread.execute {
                if (favoriteRepo.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onFavoriteReposLoaded(favoriteRepo)
                }
            }
        }
    }

    override fun saveRepo(repo: Repo) {
        appExecutors.diskIO.execute {
            githubDao.insertRepo(repo.apply {
                val favoriteRepo: FavoriteRepo? = githubDao.getFavoriteRepoById(repo.id)
                isFavorite = favoriteRepo != null
            })
        }
    }

    override fun updateFavoriteRepo(repoId: String, isFavorite: Boolean) {
        appExecutors.diskIO.execute { githubDao.updateFavoriteRepo(repoId, isFavorite) }
    }

    override fun refreshRepos() {
    }

    override fun deleteAllRepos() {
        appExecutors.diskIO.execute { githubDao.deleteRepos() }
    }

    override fun deleteRepo(repoId: String) {
        appExecutors.diskIO.execute { githubDao.deleteRepoById(repoId) }
    }

    override fun saveFavoriteRepo(repoId: String) {
        val favoriteRepo = FavoriteRepo(repoId = repoId)
        appExecutors.diskIO.execute { githubDao.insertFavoriteRepo(favoriteRepo) }
    }

    override fun deleteFavoriteRepo(repoId: String) {
        appExecutors.diskIO.execute { githubDao.deleteFavoriteRepoById(repoId) }
    }
}