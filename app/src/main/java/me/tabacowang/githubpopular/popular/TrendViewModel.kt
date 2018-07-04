package me.tabacowang.githubpopular.popular

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.graphics.drawable.Drawable
import me.tabacowang.githubpopular.SingleLiveEvent
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.source.GithubDataSource
import me.tabacowang.githubpopular.data.source.GithubRepository

class TrendViewModel(
        context: Application,
        private val githubRepository: GithubRepository
) : AndroidViewModel(context) {

    internal val openRepoEvent = SingleLiveEvent<Repo>()

    // These observable fields will update Views automatically
    val items: ObservableList<Repo> = ObservableArrayList()
    val dataLoading = ObservableBoolean(false)
    val noRepoLabel = ObservableField<String>()
    val noRepoIconRes = ObservableField<Drawable>()
    val empty = ObservableBoolean(false)
    val snackbarMessage = SingleLiveEvent<Int>()

    fun start() {
        loadTrendRepos()
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

    private fun loadTrendRepos() {
        dataLoading.set(true)

        githubRepository.getTrendRepos(object : GithubDataSource.LoadTrendReposCallback {
            override fun onTrendReposLoaded(repos: List<Repo>) {
                val reposToShow = repos

                dataLoading.set(false)

                with(items) {
                    clear()
                    addAll(reposToShow)
                    empty.set(isEmpty())
                }
            }

            override fun onDataNotAvailable() {
                dataLoading.set(false)
                empty.set(true)
            }
        })
    }
}