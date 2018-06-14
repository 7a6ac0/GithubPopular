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
import me.tabacowang.githubpopular.data.source.GithubDataSource
import me.tabacowang.githubpopular.data.source.GithubRepository

class PopularViewModel(
        context: Application,
        private val githubRepository: GithubRepository
) : AndroidViewModel(context) {

    private val isDataLoadingError = ObservableBoolean(false)
    private val context: Context = context.applicationContext //Application Context to avoid leaks.

    internal val openRepoEvent = SingleLiveEvent<String>()

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

    fun updateFavoriteRepo(repo: Repo, isFavorite: Boolean) {
        if (isFavorite) {
            githubRepository.saveFavoriteRepo(repo.id)
        }
        else {
            githubRepository.deleteFavoriteRepo(repo.id)
        }
        repo.isFavorite = isFavorite
    }

    private fun loadRepos(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            dataLoading.set(true)
        }

        if (forceUpdate) {
            // refresh repos
            githubRepository.refreshRepos()
        }

        githubRepository.getRepos(searchQuery.get()!!.toLowerCase(), object : GithubDataSource.LoadReposCallback{
            override fun onReposLoaded(repos: List<Repo>) {
                val reposToShow = repos

                if (showLoadingUI) {
                    dataLoading.set(false)
                }
                isDataLoadingError.set(false)

                with(items) {
                    clear()
                    addAll(reposToShow)
                    empty.set(isEmpty())
                }
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