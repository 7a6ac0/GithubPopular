package me.tabacowang.githubpopular.repodetail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import me.tabacowang.githubpopular.R
import me.tabacowang.githubpopular.databinding.RepodetailFragmentBinding
import me.tabacowang.githubpopular.util.obtainViewModel
import me.tabacowang.githubpopular.util.setupSnackbar

class RepoDetailFragment : Fragment() {

    private lateinit var repodetailFragmentBinding: RepodetailFragmentBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        repodetailFragmentBinding.viewmodel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
            it.noRepoLabel.set(getString(R.string.not_exist_repo))
        }
        setupWebView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val activity = activity as RepoDetailActivity
        repodetailFragmentBinding = RepodetailFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = activity.obtainViewModel(RepoDetailViewModel::class.java)
        }
        setHasOptionsMenu(true)
        activity.findViewById<TextView>(R.id.toolbar_title).apply {
            text = arguments!!.getString(ARGUMENT_REPO_FULL_NAME)
        }
        return repodetailFragmentBinding.root
    }

    override fun onResume() {
        super.onResume()
        repodetailFragmentBinding.viewmodel?.start(arguments!!.getString(ARGUMENT_REPO_ID))
    }

    private fun setupWebView() {
        repodetailFragmentBinding.webview.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    repodetailFragmentBinding.viewmodel?.dataLoading?.set(false)
                }
            }
        }
    }

    companion object {
        const val ARGUMENT_REPO_ID = "REPO_ID"
        const val ARGUMENT_REPO_FULL_NAME = "REPO_FULL_NAME"

        fun newInstance(repoId: String, repoFullName: String) = RepoDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_REPO_ID, repoId)
                putString(ARGUMENT_REPO_FULL_NAME, repoFullName)
            }
        }
    }
}