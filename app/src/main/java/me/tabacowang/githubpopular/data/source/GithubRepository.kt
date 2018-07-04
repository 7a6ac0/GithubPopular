package me.tabacowang.githubpopular.data.source

import me.tabacowang.githubpopular.data.FavoriteRepo
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.RepoSearchResult
import me.tabacowang.githubpopular.data.source.remote.TrendRemoteDataSource
import java.util.LinkedHashMap

class GithubRepository(
        private val githubRemoteDataSource: GithubDataSource,
        private val githubLocalDataSource: GithubDataSource,
        private val trendDataSource: GithubDataSource
) : GithubDataSource {

    var cachedRepos: LinkedHashMap<String, Repo> = LinkedHashMap()

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    var cacheIsDirty = false

    override fun getTrendRepos(callback: GithubDataSource.LoadTrendReposCallback) {
        deleteRepo("trending")
        trendDataSource.getTrendRepos(object : GithubDataSource.LoadTrendReposCallback {
            override fun onTrendReposLoaded(repos: List<Repo>) {
                saveToLocalDataSource(repos)
                githubLocalDataSource.getRepos("trending", 1, object : GithubDataSource.LoadReposCallback {
                    override fun onReposLoaded(repos: List<Repo>) {
                        refreshCache(repos)
                        callback.onTrendReposLoaded(ArrayList(cachedRepos.values))
                    }

                    override fun onDataNotAvailable() {

                    }
                })
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getRepos(searchQuery: String, page: Int, callback: GithubDataSource.LoadReposCallback) {
        if (cacheIsDirty) {
            deleteRepo(searchQuery)
            deleteSearchResult(searchQuery)
            getReposFromRemoteDataSource(searchQuery, page, callback)
        } else if (page > 1) {
            getReposFromRemoteDataSource(searchQuery, page, callback)
        } else {
            githubLocalDataSource.getRepos(searchQuery, page, object : GithubDataSource.LoadReposCallback {
                override fun onReposLoaded(repos: List<Repo>) {
                    refreshCache(repos)
                    callback.onReposLoaded(ArrayList(cachedRepos.values))
                }

                override fun onDataNotAvailable() {
                    getReposFromRemoteDataSource(searchQuery, page, callback)
                }
            })
        }
    }

    override fun getRepo(repoId: String, callback: GithubDataSource.GetRepoCallback) {
        val repoInCache = getRepoWithId(repoId)

        if (repoInCache != null) {
            callback.onRepoLoaded(repoInCache)
            return
        }

        githubLocalDataSource.getRepo(repoId, object : GithubDataSource.GetRepoCallback {
            override fun onRepoLoaded(repo: Repo) {
                cacheAndPerform(repo) {
                    callback.onRepoLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
//                githubRemoteDataSource.getRepo(repoId, object : GithubDataSource.GetRepoCallback {
//                    override fun onRepoLoaded(repo: Repo) {
//                        cacheAndPerform(repo) {
//                            callback.onRepoLoaded(it)
//                        }
//                    }
//
//                    override fun onDataNotAvailable() {
//                        callback.onDataNotAvailable()
//                    }
//                })
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getFavoriteRepos(callback: GithubDataSource.LoadFavoriteReposCallback) {
        githubLocalDataSource.getFavoriteRepos(object : GithubDataSource.LoadFavoriteReposCallback{
            override fun onFavoriteReposLoaded(favoriteRepos: List<FavoriteRepo>) {
                callback.onFavoriteReposLoaded(favoriteRepos)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun saveRepo(repo: Repo) {
        // For test.
        // Not require to add a repo by user.
    }

    override fun refreshRepos() {
        cacheIsDirty = true
    }

    override fun deleteAllRepos() {
        // For test.
//        githubLocalDataSource.deleteAllRepos()
//        cachedRepos.clear()
    }

    override fun deleteRepo(searchQuery: String) {
        githubLocalDataSource.deleteRepo(searchQuery)
    }

    override fun updateFavoriteRepo(favoriteRepo: Repo, isFavorite: Boolean) {
        getRepoWithId(favoriteRepo.id)?.let {
            cacheAndPerform(it) {
                githubLocalDataSource.updateFavoriteRepo(it, isFavorite)
            }
        }
    }

    override fun saveFavoriteRepo(favoriteRepo: Repo) {
        githubLocalDataSource.saveFavoriteRepo(favoriteRepo)
        updateFavoriteRepo(favoriteRepo, true)
    }

    override fun deleteFavoriteRepo(favoriteRepo: Repo) {
        githubLocalDataSource.deleteFavoriteRepo(favoriteRepo)
        updateFavoriteRepo(favoriteRepo, false)
    }

    override fun saveSearchResult(repoSearchResult: RepoSearchResult) {
        githubLocalDataSource.saveSearchResult(repoSearchResult)
    }

    override fun getSearchResult(searchQuery: String, callback: GithubDataSource.GetSearchResultCallback) {
        githubLocalDataSource.getSearchResult(searchQuery, object : GithubDataSource.GetSearchResultCallback {
            override fun onResultLoaded(repoSearchResult: RepoSearchResult) {
                callback.onResultLoaded(repoSearchResult)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun deleteSearchResult(searchQuery: String) {
        githubLocalDataSource.deleteSearchResult(searchQuery)
    }

    private fun getReposFromRemoteDataSource(searchQuery: String, page: Int, callback: GithubDataSource.LoadReposCallback) {
        githubRemoteDataSource.getRepos(searchQuery, page, object : GithubDataSource.LoadReposCallback{
            override fun onReposLoaded(repos: List<Repo>) {
                saveToLocalDataSource(repos)
                githubLocalDataSource.getRepos(searchQuery, page, object : GithubDataSource.LoadReposCallback {
                    override fun onReposLoaded(repos: List<Repo>) {
                        refreshCache(repos)
                        callback.onReposLoaded(ArrayList(cachedRepos.values))
                    }

                    override fun onDataNotAvailable() {

                    }
                })
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
                cacheIsDirty = false
            }
        })
    }

    private fun refreshCache(repos: List<Repo>) {
        cachedRepos.clear()
        repos.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun saveToLocalDataSource(repos: List<Repo>) {
        for (repo in repos) {
            githubLocalDataSource.saveRepo(repo)
        }
    }

    private fun getRepoWithId(id: String) = cachedRepos[id]

    private inline fun cacheAndPerform(repo: Repo, perform: (Repo) -> Unit) {
        val cachedRepo = Repo(repo.id, repo.name, repo.fullName, repo.description, repo.owner, repo.htmlUrl, repo.stars).apply {
            searchQuery = repo.searchQuery
            isFavorite = repo.isFavorite
        }
        cachedRepos[repo.id] = cachedRepo
        perform(cachedRepo)
    }

    companion object {

        private var INSTANCE: GithubRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param githubRemoteDataSource the backend data source
         * *
         * @param githubLocalDataSource  the device storage data source
         * *
         * @return the [GithubRepository] instance
         */
        @JvmStatic
        fun getInstance(githubRemoteDataSource: GithubDataSource,
                        githubLocalDataSource: GithubDataSource,
                        trendDataSource: GithubDataSource) =
                INSTANCE ?: synchronized(GithubRepository::class.java) {
                    INSTANCE ?: GithubRepository(githubRemoteDataSource, githubLocalDataSource, trendDataSource)
                            .also { INSTANCE = it }
                }


        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}