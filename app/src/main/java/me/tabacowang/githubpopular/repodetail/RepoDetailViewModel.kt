package me.tabacowang.githubpopular.repodetail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.drawable.Drawable
import me.tabacowang.githubpopular.SingleLiveEvent
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.data.source.GithubDataSource
import me.tabacowang.githubpopular.data.source.GithubRepository

class RepoDetailViewModel(
        context: Application,
        private val githubRepository: GithubRepository
) : AndroidViewModel(context), GithubDataSource.GetRepoCallback {

    val repoUrl = ObservableField<String>()
    val dataLoading = ObservableBoolean(true)
    val noRepoLabel = ObservableField<String>()
    val noRepoIconRes = ObservableField<Drawable>()
    val empty = ObservableBoolean(false)
    val snackbarMessage = SingleLiveEvent<Int>()

    override fun onRepoLoaded(repo: Repo) {
        repoUrl.set(repo.htmlUrl)
        empty.set(false)
    }

    override fun onDataNotAvailable() {
        repoUrl.set(null)
        empty.set(true)
    }

    fun start(repoId: String?) {
        repoId?.let {
            githubRepository.getRepo(it, this)
        }
    }
}