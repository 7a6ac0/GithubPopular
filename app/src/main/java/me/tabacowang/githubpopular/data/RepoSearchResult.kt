package me.tabacowang.githubpopular.data

import android.arch.persistence.room.Entity

@Entity(tableName = "repo_search_result",
        primaryKeys = ["searchQuery"])
data class RepoSearchResult(
        val searchQuery: String,
        val next: Int?
)