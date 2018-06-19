package me.tabacowang.githubpopular.data.source.remote

import com.google.common.collect.Lists
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.RepoSearchResult
import me.tabacowang.githubpopular.data.source.GithubDataSource
import me.tabacowang.githubpopular.util.AppExecutors
import java.util.LinkedHashMap

object GithubRemoteDataSource : GithubDataSource {

    private var REPOS_SERVICE_DATA: LinkedHashMap<String, Repo> = LinkedHashMap()

    private val githubService by lazy { GithubServiceFactory.APIService }

    private val appExecutors by lazy { AppExecutors() }

    override fun getRepos(searchQuery: String, page: Int, callback: GithubDataSource.LoadReposCallback) {
        appExecutors.networkIO.execute {
            githubService.getRepos("topic:$searchQuery", page = page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe ({githubResponse ->
                        deleteAllRepos()
                        githubResponse.items.forEach {repo ->
                            REPOS_SERVICE_DATA[repo.id] = repo.apply {
                                this.searchQuery = searchQuery
                            }
                        }
                        callback.onReposLoaded(Lists.newArrayList(REPOS_SERVICE_DATA.values))
                    }, {
                        it.printStackTrace()
                        callback.onDataNotAvailable()
                    })
        }
    }

    override fun getRepo(repoId: String, callback: GithubDataSource.GetRepoCallback) {
//        val repo = REPOS_SERVICE_DATA[repoId]
//        repo?.let { callback.onRepoLoaded(repo) }
    }

    override fun getFavoriteRepos(callback: GithubDataSource.LoadFavoriteReposCallback) {

    }

    override fun saveRepo(repo: Repo) {

    }

    override fun updateFavoriteRepo(repoId: String, isFavorite: Boolean) {

    }

    override fun refreshRepos() {
    }

    override fun deleteAllRepos() {
        REPOS_SERVICE_DATA.clear()
    }

    override fun deleteRepo(searchQuery: String) {

    }

    override fun saveFavoriteRepo(repoId: String) {

    }

    override fun deleteFavoriteRepo(repoId: String) {

    }

    override fun saveSearchResult(repoSearchResult: RepoSearchResult) {

    }

    override fun getSearchResult(searchQuery: String, callback: GithubDataSource.GetSearchResultCallback) {

    }

    override fun deleteSearchResult(searchQuery: String) {

    }
}