package me.tabacowang.githubpopular.popular

import android.databinding.BindingAdapter
import android.widget.ListView
import me.tabacowang.githubpopular.data.Repo

object RepoListBinding {
    @BindingAdapter("app:items")
    @JvmStatic fun setItems(listView: ListView, items: List<Repo>) {
        with(listView.adapter as RepoAdapter) {
            replaceData(items)
        }
    }
}