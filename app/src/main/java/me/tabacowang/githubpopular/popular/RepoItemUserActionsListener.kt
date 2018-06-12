package me.tabacowang.githubpopular.popular

import me.tabacowang.githubpopular.data.Repo

interface RepoItemUserActionsListener {

    fun onRepoClicked(repo: Repo)
}