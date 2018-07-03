package me.tabacowang.githubpopular.data.source.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET(GithubSetting.SEARCH_REPOSITORIES)
    fun getRepos(@Query(GithubSetting.QUERY) queryString: String,
                 @Query(GithubSetting.SORT) sort: String = "stars",
                 @Query(GithubSetting.PAGE) page: Int = 1): Observable<GithubResponse>
}