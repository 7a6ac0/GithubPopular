package me.tabacowang.githubpopular

import android.app.Application

class GithubPopularApplication : Application() {
    companion object {
        lateinit var instance: GithubPopularApplication private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}