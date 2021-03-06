package me.tabacowang.githubpopular.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import java.util.*

@Entity(tableName = "favorite_repos",
        primaryKeys = ["_id"])
data class FavoriteRepo(
        val _id: String = UUID.randomUUID().toString(),
        @Embedded
        val repo: Repo
)