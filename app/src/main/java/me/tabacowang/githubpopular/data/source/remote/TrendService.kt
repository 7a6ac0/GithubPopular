package me.tabacowang.githubpopular.data.source.remote

import io.reactivex.Observable
import me.tabacowang.githubpopular.data.Repo

interface TrendService {
    fun getTrendRepos(): Observable<List<Repo>>
}