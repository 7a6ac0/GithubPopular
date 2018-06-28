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
import me.tabacowang.githubpopular.data.FavoriteRepo
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.source.GithubDataSource
import me.tabacowang.githubpopular.data.source.GithubRepository

class FavoriteViewModel(
        context: Application,
        private val githubRepository: GithubRepository
) : AndroidViewModel(context) {

    private val isDataLoadingError = ObservableBoolean(false)
    private val context: Context = context.applicationContext //Application Context to avoid leaks.

    internal val openRepoEvent = SingleLiveEvent<Repo>()

    // These observable fields will update Views automatically
    val items: ObservableList<Repo> = ObservableArrayList()
    val noRepoLabel = ObservableField<String>()
    val noRepoIconRes = ObservableField<Drawable>()
    val empty = ObservableBoolean(false)
    val snackbarMessage = SingleLiveEvent<Int>()

    fun start() {
        loadFavoriteRepos()
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

    private fun loadFavoriteRepos() {
        githubRepository.getFavoriteRepos(object : GithubDataSource.LoadFavoriteReposCallback{
            override fun onFavoriteReposLoaded(favoriteRepos: List<FavoriteRepo>) {
                var favoriteReposToShow: ArrayList<Repo> = ArrayList<Repo>()
                favoriteRepos.forEach {
                    favoriteReposToShow.add(it.repo)
                }

                with(items) {
                    clear()
                    addAll(favoriteReposToShow)
                    empty.set(isEmpty())
                }
            }

            override fun onDataNotAvailable() {
                empty.set(true)
            }
        })
    }
}