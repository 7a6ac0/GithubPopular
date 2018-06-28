package me.tabacowang.githubpopular.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repos",
        indices = [
        Index("id"),
        Index("owner_login")],
        primaryKeys = ["name", "owner_login", "searchQuery"]
)
data class Repo(
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("full_name")
        val fullName: String,
        @SerializedName("description")
        val description: String?,
        @SerializedName("owner")
        @Embedded(prefix = "owner_")
        val owner: Owner,
        @SerializedName("html_url")
        val htmlUrl: String,
        @SerializedName("stargazers_count")
        val stars: Int
) {

    data class Owner(
            @SerializedName("login")
            val login: String,
            @SerializedName("avatar_url")
            val avatarUrl: String,
            @SerializedName("url")
            val url: String?
    )

    var searchQuery: String = ""

    var isFavorite = false

    companion object {
        const val UNKNOWN_ID = -1
    }
}