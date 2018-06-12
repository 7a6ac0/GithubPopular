package me.tabacowang.githubpopular.data.source.remote

import com.google.gson.annotations.SerializedName
import me.tabacowang.githubpopular.data.Repo

data class GithubResponse(
        @SerializedName("total_count") var total: Int = 0,
        @SerializedName("items") var items: List<Repo>
) {
    var nextPage: Int? = null
}