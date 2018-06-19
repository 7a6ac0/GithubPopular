package me.tabacowang.githubpopular.popular

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import me.tabacowang.githubpopular.R
import me.tabacowang.githubpopular.databinding.PopularFragmentBinding
import me.tabacowang.githubpopular.util.obtainViewModel
import me.tabacowang.githubpopular.util.setupSnackbar

class PopularFragment : Fragment() {
    private lateinit var popularFragmentBinding: PopularFragmentBinding
    private lateinit var listAdapter: RepoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        popularFragmentBinding = PopularFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = (activity as PopularActivity).obtainViewModel(PopularViewModel::class.java)
        }
        setHasOptionsMenu(true)
        return popularFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        popularFragmentBinding.viewmodel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
            it.noRepoLabel.set(getString(R.string.no_repos))
        }
        setupListAdapter()
        setupRefreshLayout()
        setupSearchView()
    }

    private fun setupListAdapter() {
        val viewModel = popularFragmentBinding.viewmodel
        if (viewModel != null) {
            listAdapter = RepoAdapter(ArrayList(0), viewModel)
            popularFragmentBinding.repoList.apply {
                adapter = listAdapter
                setOnScrollListener(object : AbsListView.OnScrollListener {
                    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                        when(scrollState) {
                            AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                                if(lastVisiblePosition == (count - 1)) {
                                    viewModel.loadMoreRepos()
                                }
                            }
                        }
                    }

                    override fun onScroll(view: AbsListView?,
                                          firstVisibleItem: Int,
                                          visibleItemCount: Int,
                                          totalItemCount: Int) {

                    }
                })
            }
        }
        else {
            Log.w(TAG, "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupRefreshLayout() {
        popularFragmentBinding.refreshLayout.run {
            setColorSchemeColors(
                    ContextCompat.getColor(activity!!, R.color.colorPrimary),
                    ContextCompat.getColor(activity!!, R.color.colorAccent),
                    ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
            )

            scrollUpChild = popularFragmentBinding.repoList
        }
    }

    private fun setupSearchView() {
        popularFragmentBinding.repoSearch.apply {
            isActivated = true
            onActionViewExpanded()
            isSubmitButtonEnabled = true
            queryHint = "Ex. kotlin"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String): Boolean {
                    popularFragmentBinding.viewmodel?.let {
                        it.searchQuery.set(query)
                        it.start()
                    }
                    clearFocus()
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    return false
                }
            })
            clearFocus()
            setQuery("kotlin", true)
        }
    }

    companion object {
        fun newInstance() = PopularFragment()
        private const val TAG = "PopularFragment"

    }
}