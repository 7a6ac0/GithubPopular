package me.tabacowang.githubpopular.popular

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import me.tabacowang.githubpopular.R
import me.tabacowang.githubpopular.data.Repo
import me.tabacowang.githubpopular.repodetail.RepoDetailActivity
import me.tabacowang.githubpopular.util.obtainViewModel
import me.tabacowang.githubpopular.util.replaceFragmentInActivity
import me.tabacowang.githubpopular.util.setupActionBar

class PopularActivity : AppCompatActivity(), RepoItemNavigator {

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popular_activity)

        setupActionBar(R.id.toolbar) {
            setDisplayShowTitleEnabled(false)
        }

        setupNavigationView()

        setupPopularFragment()

        setupPopularViewModel()

    }

    private fun setupNavigationView() {
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_view).apply {
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_popular -> {
                        if(supportFragmentManager.findFragmentById(R.id.contentFrame) !is PopularFragment) {
                            PopularFragment.newInstance().let {
                                replaceFragmentInActivity(it, R.id.contentFrame)
                            }
                            setupPopularViewModel()
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.navigation_favorite -> {
                        if(supportFragmentManager.findFragmentById(R.id.contentFrame) !is FavoriteFragment) {
                            FavoriteFragment.newInstance().let {
                                replaceFragmentInActivity(it, R.id.contentFrame)
                            }
                            setupFavoriteViewModel()
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.navigation_trend -> {
                        if(supportFragmentManager.findFragmentById(R.id.contentFrame) !is TrendFragment) {
                            TrendFragment.newInstance().let {
                                replaceFragmentInActivity(it, R.id.contentFrame)
                            }
                            setupTrendViewModel()
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                }
                false
            }
        }
    }

    private fun setupPopularFragment() {
        supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
        PopularFragment.newInstance().let {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }
    }

    private fun setupPopularViewModel() {
        viewModel = obtainViewModel(PopularViewModel::class.java).apply {
            openRepoEvent.observe(this@PopularActivity, Observer<Repo> { repo ->
                if (repo != null) {
                    openRepoDetails(repo.id, repo.fullName)
                }
            })
        }
    }

    private fun setupFavoriteViewModel() {
        viewModel = obtainViewModel(FavoriteViewModel::class.java).apply {
            openRepoEvent.observe(this@PopularActivity, Observer<Repo> { repo ->
                if (repo != null) {
                    openRepoDetails(repo.id, repo.fullName)
                }
            })
        }
    }

    private fun setupTrendViewModel() {
        viewModel = obtainViewModel(TrendViewModel::class.java).apply {
            openRepoEvent.observe(this@PopularActivity, Observer<Repo> { repo ->
                if (repo != null) {
                    openRepoDetails(repo.id, repo.fullName)
                }
            })
        }
    }

    override fun openRepoDetails(repoId: String, repoFullName: String) {
        val intent = Intent(this, RepoDetailActivity::class.java).apply {
            putExtra(RepoDetailActivity.EXTRA_REPO_ID, repoId)
            putExtra(RepoDetailActivity.EXTRA_REPO_FULL_NAME, repoFullName)
        }
        startActivity(intent)
    }

}
