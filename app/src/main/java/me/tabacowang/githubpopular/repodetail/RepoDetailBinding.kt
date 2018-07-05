package me.tabacowang.githubpopular.repodetail

import android.databinding.BindingAdapter
import android.webkit.WebView

object RepoDetailBinding {
    @BindingAdapter("app:loadUrl")
    @JvmStatic fun setURL(webView: WebView, url: String?) {
        with(webView) {
            loadUrl(url)
        }
    }
}