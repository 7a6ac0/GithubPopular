package me.tabacowang.githubpopular.popular

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.tabacowang.githubpopular.databinding.FavoriteFragmentBinding
import me.tabacowang.githubpopular.util.obtainViewModel

class FavoriteFragment : Fragment() {
    private lateinit var favoriteFragmentBinding: FavoriteFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        favoriteFragmentBinding = FavoriteFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = this@FavoriteFragment.obtainViewModel(FavoriteViewModel::class.java)
        }
        setHasOptionsMenu(true)
        return favoriteFragmentBinding.root
    }

    override fun onResume() {
        super.onResume()
        favoriteFragmentBinding.viewmodel?.start()
    }

    companion object {
        fun newInstance() = FavoriteFragment()
        private const val TAG = "FavoriteFragment"

    }

}