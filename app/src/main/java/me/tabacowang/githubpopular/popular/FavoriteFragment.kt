package me.tabacowang.githubpopular.popular

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.tabacowang.githubpopular.R
import me.tabacowang.githubpopular.databinding.FavoriteFragmentBinding
import me.tabacowang.githubpopular.util.obtainViewModel
import me.tabacowang.githubpopular.util.setupSnackbar

class FavoriteFragment : Fragment() {
    private lateinit var favoriteFragmentBinding: FavoriteFragmentBinding
    private lateinit var listAdapter: RepoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        favoriteFragmentBinding = FavoriteFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = (activity as PopularActivity).obtainViewModel(FavoriteViewModel::class.java)
        }
        setHasOptionsMenu(true)
        return favoriteFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteFragmentBinding.viewmodel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
            it.noRepoLabel.set(getString(R.string.no_favorite_repos))
        }
        setupListAdapter()
    }

    override fun onResume() {
        super.onResume()
        favoriteFragmentBinding.viewmodel?.start()
    }

    private fun setupListAdapter() {
        val viewModel = favoriteFragmentBinding.viewmodel
        if (viewModel != null) {
            listAdapter = RepoAdapter(ArrayList(0), viewModel)
            favoriteFragmentBinding.repoList.adapter = listAdapter
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()
        private const val TAG = "FavoriteFragment"

    }

}