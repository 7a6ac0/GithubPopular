package me.tabacowang.githubpopular.data.source.remote

import com.google.common.collect.Lists
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.RepoSearchResult
import me.tabacowang.githubpopular.data.source.GithubDataSource
import me.tabacowang.githubpopular.util.AppExecutors
import java.util.LinkedHashMap

object TrendRemoteDataSource : GithubDataSource {
    private var REPOS_SERVICE_DATA: LinkedHashMap<String, Repo> = LinkedHashMap()

    private val trendService by lazy { GithubServiceFactory.trendingService }

    private val appExecutors by lazy { AppExecutors() }

    override fun getTrendRepos(callback: GithubDataSource.LoadTrendReposCallback) {
        appExecutors.networkIO.execute {
            trendService.getTrendRepos()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe ({trendResponse ->
                        deleteAllRepos()
                        trendResponse.forEach { repo ->
                            REPOS_SERVICE_DATA[repo.id] = repo
                        }
                        callback.onTrendReposLoaded(Lists.newArrayList(REPOS_SERVICE_DATA.values))
                    }, {
                        it.printStackTrace()
                        callback.onDataNotAvailable()
                    })
        }
    }

    override fun getRepos(searchQuery: String, page: Int, callback: GithubDataSource.LoadReposCallback) {

    }

    override fun getRepo(repoId: String, callback: GithubDataSource.GetRepoCallback) {

    }

    override fun getFavoriteRepos(callback: GithubDataSource.LoadFavoriteReposCallback) {

    }

    override fun saveRepo(repo: Repo) {

    }

    override fun refreshRepos() {
    }

    override fun deleteAllRepos() {
        REPOS_SERVICE_DATA.clear()
    }

    override fun deleteRepo(searchQuery: String) {

    }

    override fun updateFavoriteRepo(favoriteRepo: Repo, isFavorite: Boolean) {

    }

    override fun saveFavoriteRepo(favoriteRepo: Repo) {

    }

    override fun deleteFavoriteRepo(favoriteRepo: Repo) {

    }

    override fun saveSearchResult(repoSearchResult: RepoSearchResult) {

    }

    override fun getSearchResult(searchQuery: String, callback: GithubDataSource.GetSearchResultCallback) {

    }

    override fun deleteSearchResult(searchQuery: String) {

    }
}