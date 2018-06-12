package me.tabacowang.githubpopular

import android.content.Context
import me.tabacowang.githubpopular.data.source.GithubRepository
import me.tabacowang.githubpopular.data.source.local.GithubLocalDataSource
import me.tabacowang.githubpopular.data.source.local.PopularDatabase
import me.tabacowang.githubpopular.data.source.remote.GithubRemoteDataSource
import me.tabacowang.githubpopular.util.AppExecutors

object Injection {

    fun providerGithubRepository(context: Context): GithubRepository {
        val database = PopularDatabase.getInstance(context)
        return GithubRepository.getInstance(GithubRemoteDataSource,
                GithubLocalDataSource.getInstance(AppExecutors(), database.popularDao()))
    }
}