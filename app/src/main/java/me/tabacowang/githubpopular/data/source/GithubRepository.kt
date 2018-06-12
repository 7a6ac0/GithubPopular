package me.tabacowang.githubpopular.data.source

import me.tabacowang.githubpopular.data.Repo
import java.util.LinkedHashMap

class GithubRepository(
        val githubRemoteDataSource: GithubDataSource,
        val githubLocalDataSource: GithubDataSource
) : GithubDataSource {

    var cachedRepos: LinkedHashMap<String, Repo> = LinkedHashMap()

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    var cacheIsDirty = false

    override fun getRepos(searchQuery: String, callback: GithubDataSource.LoadReposCallback) {
//        if (cachedRepos.isNotEmpty() && !cacheIsDirty) {
//            callback.onReposLoaded(ArrayList(cachedRepos.values))
//            return
//        }

        if (cacheIsDirty) {
            getReposFromRemoteDataSource(searchQuery, callback)
        } else {
            githubLocalDataSource.getRepos(searchQuery, object : GithubDataSource.LoadReposCallback {
                override fun onReposLoaded(repos: List<Repo>) {
                    refreshCache(repos)
                    callback.onReposLoaded(ArrayList(cachedRepos.values))
                }

                override fun onDataNotAvailable() {
                    getReposFromRemoteDataSource(searchQuery, callback)
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
                githubRemoteDataSource.getRepo(repoId, object : GithubDataSource.GetRepoCallback {
                    override fun onRepoLoaded(repo: Repo) {
                        cacheAndPerform(repo) {
                            callback.onRepoLoaded(it)
                        }
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
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

    override fun deleteRepo(repoId: String) {
        // For test.
//        githubLocalDataSource.deleteRepo(repoId)
//        cachedRepos.remove(repoId)
    }

    private fun getReposFromRemoteDataSource(searchQuery: String, callback: GithubDataSource.LoadReposCallback) {
        githubRemoteDataSource.getRepos(searchQuery, object : GithubDataSource.LoadReposCallback{
            override fun onReposLoaded(repos: List<Repo>) {
                refreshCache(repos)
                saveToLocalDataSource(repos)
                callback.onReposLoaded(ArrayList(cachedRepos.values))
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
        val cachedRepo = Repo(repo.id, repo.name, repo.fullName, repo.description, repo.owner, repo.stars)
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
                                   githubLocalDataSource: GithubDataSource) =
                INSTANCE ?: synchronized(GithubRepository::class.java) {
                    INSTANCE ?: GithubRepository(githubRemoteDataSource, githubLocalDataSource)
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