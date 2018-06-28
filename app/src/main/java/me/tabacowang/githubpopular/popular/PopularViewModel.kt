package me.tabacowang.githubpopular.popular

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.graphics.drawable.Drawable
import me.tabacowang.githubpopular.SingleLiveEvent
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.RepoSearchResult
import me.tabacowang.githubpopular.data.source.GithubDataSource
import me.tabacowang.githubpopular.data.source.GithubRepository

class PopularViewModel(
        context: Application,
        private val githubRepository: GithubRepository
) : AndroidViewModel(context) {

    private val isDataLoadingError = ObservableBoolean(false)
    private val context: Context = context.applicationContext //Application Context to avoid leaks.

    internal val openRepoEvent = SingleLiveEvent<Repo>()

    val searchQuery = ObservableField<String>("")

    // These observable fields will update Views automatically
    val items: ObservableList<Repo> = ObservableArrayList()
    val dataLoading = ObservableBoolean(false)
    val noRepoLabel = ObservableField<String>()
    val noRepoIconRes = ObservableField<Drawable>()
    val empty = ObservableBoolean(true)
    val snackbarMessage = SingleLiveEvent<Int>()

    fun start() {
        loadRepos(false)
    }

    fun loadRepos(forceUpdate: Boolean) {
        loadRepos(forceUpdate, true)
    }

    fun loadMoreRepos() {
        val query = searchQuery.get()!!.toLowerCase()
        githubRepository.getSearchResult(query, object : GithubDataSource.GetSearchResultCallback {
            override fun onResultLoaded(repoSearchResult: RepoSearchResult) {
                loadMoreRepos(query, repoSearchResult.next!!)
            }

            override fun onDataNotAvailable() {

            }
        })
    }

    fun updateFavoriteRepo(repo: Repo, isFavorite: Boolean) {
        if (isFavorite) {
            githubRepository.saveFavoriteRepo(repo)
        }
        else {
            githubRepository.deleteFavoriteRepo(repo)
        }
        repo.isFavorite = isFavorite
    }

    private fun loadMoreRepos(searchQuery: String, page: Int) {
        dataLoading.set(true)

        githubRepository.getRepos(searchQuery, page, object : GithubDataSource.LoadReposCallback{
            override fun onReposLoaded(repos: List<Repo>) {
                val reposToShow = repos
                val searchResult = RepoSearchResult(searchQuery, page + 1)

                dataLoading.set(false)
                isDataLoadingError.set(false)

                with(items) {
                    clear()
                    addAll(reposToShow)
                }

                githubRepository.saveSearchResult(searchResult)
            }

            override fun onDataNotAvailable() {
                isDataLoadingError.set(true)
                dataLoading.set(false)
            }
        })
    }

    private fun loadRepos(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            dataLoading.set(true)
        }

        if (forceUpdate) {
            // refresh repos
            githubRepository.refreshRepos()
        }

        val query = searchQuery.get()!!.toLowerCase()

        githubRepository.getRepos(query, 1, object : GithubDataSource.LoadReposCallback{
            override fun onReposLoaded(repos: List<Repo>) {
                val reposToShow = repos
                val searchResult = RepoSearchResult(query, 2)

                if (showLoadingUI) {
                    dataLoading.set(false)
                }
                isDataLoadingError.set(false)

                with(items) {
                    clear()
                    addAll(reposToShow)
                    empty.set(isEmpty())
                }

                githubRepository.getSearchResult(query, object : GithubDataSource.GetSearchResultCallback {
                    override fun onResultLoaded(repoSearchResult: RepoSearchResult) {

                    }

                    override fun onDataNotAvailable() {
                        githubRepository.saveSearchResult(searchResult)
                    }
                })

            }

            override fun onDataNotAvailable() {
                isDataLoadingError.set(true)
                if (showLoadingUI) {
                    dataLoading.set(false)
                }
                empty.set(true)

            }
        })
    }


}