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
import me.tabacowang.githubpopular.data.source.GithubRepository

class TrendViewModel(
        context: Application,
        private val githubRepository: GithubRepository
) : AndroidViewModel(context) {

    internal val openRepoEvent = SingleLiveEvent<Repo>()

    // These observable fields will update Views automatically
    val items: ObservableList<Repo> = ObservableArrayList()
    val noRepoLabel = ObservableField<String>()
    val noRepoIconRes = ObservableField<Drawable>()
    val empty = ObservableBoolean(false)
    val snackbarMessage = SingleLiveEvent<Int>()

    fun start() {
        loadTrendRepos()
    }

    private fun loadTrendRepos() {

    }
}