package me.tabacowang.githubpopular

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import me.tabacowang.githubpopular.data.source.GithubRepository
import me.tabacowang.githubpopular.popular.FavoriteViewModel
import me.tabacowang.githubpopular.popular.PopularViewModel
import me.tabacowang.githubpopular.popular.TrendViewModel
import me.tabacowang.githubpopular.repodetail.RepoDetailViewModel

class ViewModelFactory private constructor(
        private val application: Application,
        private val githubRepository: GithubRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(PopularViewModel::class.java) ->
                    PopularViewModel(application, githubRepository)
                isAssignableFrom(FavoriteViewModel::class.java) ->
                    FavoriteViewModel(application, githubRepository)
                isAssignableFrom(RepoDetailViewModel::class.java) ->
                        RepoDetailViewModel(application, githubRepository)
                isAssignableFrom(TrendViewModel::class.java) ->
                        TrendViewModel(application, githubRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application,
                            Injection.providerGithubRepository(application.applicationContext))
                            .also { INSTANCE = it }
                }
    }
}