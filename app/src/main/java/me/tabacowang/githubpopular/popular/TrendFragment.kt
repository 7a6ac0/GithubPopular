package me.tabacowang.githubpopular.popular

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.tabacowang.githubpopular.R
import me.tabacowang.githubpopular.databinding.TrendFragmentBinding
import me.tabacowang.githubpopular.util.obtainViewModel
import me.tabacowang.githubpopular.util.setupSnackbar

class TrendFragment : Fragment() {
    private lateinit var trendFragmentBinding: TrendFragmentBinding
    private lateinit var listAdapter: RepoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        trendFragmentBinding = TrendFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = (activity as PopularActivity).obtainViewModel(TrendViewModel::class.java)
        }
        setHasOptionsMenu(true)
        return trendFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        trendFragmentBinding.viewmodel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
            it.noRepoLabel.set(getString(R.string.no_trending_repos))
        }
        setupListAdapter()
        setupRefreshLayout()
    }

    override fun onResume() {
        super.onResume()
        trendFragmentBinding.viewmodel?.start()
    }

    private fun setupListAdapter() {
        val viewModel = trendFragmentBinding.viewmodel
        if (viewModel != null) {
            listAdapter = RepoAdapter(ArrayList(0), viewModel)
            trendFragmentBinding.repoList.adapter = listAdapter
        }
    }

    private fun setupRefreshLayout() {
        trendFragmentBinding.refreshLayout.run {
            setColorSchemeColors(
                    ContextCompat.getColor(activity!!, R.color.colorPrimary),
                    ContextCompat.getColor(activity!!, R.color.colorAccent),
                    ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
            )

            scrollUpChild = trendFragmentBinding.repoList
        }
    }

    companion object {
        fun newInstance() = TrendFragment()
        private const val TAG = "TrendFragment"
    }

}