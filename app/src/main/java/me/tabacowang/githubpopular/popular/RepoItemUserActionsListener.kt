package me.tabacowang.githubpopular.popular

import android.view.View
import me.tabacowang.githubpopular.data.Repo

interface RepoItemUserActionsListener {

    fun onRepoClicked(repo: Repo)

    fun onFavoriteChanged(repo: Repo, v: View)
}