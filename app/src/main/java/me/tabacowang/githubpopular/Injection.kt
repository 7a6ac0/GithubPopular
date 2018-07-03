package me.tabacowang.githubpopular

import android.content.Context
import me.tabacowang.githubpopular.data.source.GithubRepository
import me.tabacowang.githubpopular.data.source.local.GithubLocalDataSource
import me.tabacowang.githubpopular.data.source.local.GithubDatabase
import me.tabacowang.githubpopular.data.source.remote.GithubRemoteDataSource
import me.tabacowang.githubpopular.data.source.remote.TrendRemoteDataSource
import me.tabacowang.githubpopular.util.AppExecutors

object Injection {

    fun providerGithubRepository(context: Context): GithubRepository {
        val database = GithubDatabase.getInstance(context)
        return GithubRepository.getInstance(
                GithubRemoteDataSource,
                GithubLocalDataSource.getInstance(AppExecutors(), database.githubDao()),
                TrendRemoteDataSource)
    }
}