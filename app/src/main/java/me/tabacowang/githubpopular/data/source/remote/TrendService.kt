package me.tabacowang.githubpopular.data.source.remote

import io.reactivex.Observable
import me.tabacowang.githubpopular.data.Repo
import retrofit2.http.GET

interface TrendService {
    @GET(GithubSetting.TREND_REPOSITORIES)
    fun getTrendRepos(): Observable<List<Repo>>
}