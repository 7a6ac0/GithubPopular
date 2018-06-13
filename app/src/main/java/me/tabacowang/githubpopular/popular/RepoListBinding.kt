package me.tabacowang.githubpopular.popular

import android.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.ListView
import com.squareup.picasso.Picasso
import me.tabacowang.githubpopular.R
import me.tabacowang.githubpopular.data.Repo

object RepoListBinding {
    @BindingAdapter("app:items")
    @JvmStatic fun setItems(listView: ListView, items: List<Repo>) {
        with(listView.adapter as RepoAdapter) {
            replaceData(items)
        }
    }

    @BindingAdapter("android:src")
    @JvmStatic fun setImage(imageView: ImageView, avatarUrl: String) {
        Picasso.with(imageView.context)
                .load(avatarUrl)
                .into(imageView)
    }
}