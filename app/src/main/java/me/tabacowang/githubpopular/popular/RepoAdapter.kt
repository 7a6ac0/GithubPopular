package me.tabacowang.githubpopular.popular

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.BaseAdapter
import android.widget.ToggleButton
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.databinding.RepoItemBinding

class RepoAdapter(
        private var repos: List<Repo>,
        private val viewModel: ViewModel
) : BaseAdapter() {

    fun replaceData(repos: List<Repo>) {
        setList(repos)
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val repoItemBinding: RepoItemBinding
        if (view == null) {
            // Inflate
            val inflater = LayoutInflater.from(viewGroup.context)
            // Create the binding
            repoItemBinding = RepoItemBinding.inflate(inflater, viewGroup, false)
        }
        else {
            // Recycling view
            repoItemBinding = DataBindingUtil.getBinding<RepoItemBinding>(view)!!
        }

        var userActionsListener = object : RepoItemUserActionsListener {
            override fun onRepoClicked(repo: Repo) {
                when(viewModel) {
                    is PopularViewModel -> {
                        viewModel.openRepoEvent.value = repo
                    }
                    is FavoriteViewModel -> {
                        viewModel.openRepoEvent.value = repo
                    }
                }
            }

            override fun onFavoriteChanged(repo: Repo, v: View) {
                val toggleButton = (v as ToggleButton).apply {
                    val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f,
                            Animation.RELATIVE_TO_SELF,
                            0.7f,
                            Animation.RELATIVE_TO_SELF,
                            0.7f).apply {
                        duration = 500
                        interpolator = BounceInterpolator()
                    }
                    startAnimation(scaleAnimation)
                }
                val checked = toggleButton.isChecked
                when(viewModel) {
                    is PopularViewModel -> {
                        viewModel.updateFavoriteRepo(repo, checked)
                    }
                    is FavoriteViewModel -> {
                        viewModel.updateFavoriteRepo(repo, checked)
                        viewModel.start()
                    }
                }
            }
        }

        with(repoItemBinding) {
            repo = getItem(position)
            listener = userActionsListener
            executePendingBindings()
        }

        return repoItemBinding.root
    }

    override fun getItem(position: Int) = repos[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = repos.size

    private fun setList(repos: List<Repo>) {
        this.repos = repos
        notifyDataSetChanged()
    }
}