package me.tabacowang.githubpopular.repodetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.tabacowang.githubpopular.R
import me.tabacowang.githubpopular.util.obtainViewModel
import me.tabacowang.githubpopular.util.replaceFragmentInActivity
import me.tabacowang.githubpopular.util.setupActionBar

class RepoDetailActivity : AppCompatActivity() {

    private lateinit var repoDetailViewModel: RepoDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repodetail_activity)

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        setupRepoDetailFragment()

        repoDetailViewModel = obtainViewModel(RepoDetailViewModel::class.java).apply {

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupRepoDetailFragment() {
        supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
                RepoDetailFragment.newInstance(
                        intent.getStringExtra(EXTRA_REPO_ID),
                        intent.getStringExtra(EXTRA_REPO_FULL_NAME)
                ).let {
                    replaceFragmentInActivity(it, R.id.contentFrame)
                }
    }

    companion object {
        const val EXTRA_REPO_ID = "REPO_ID"
        const val EXTRA_REPO_FULL_NAME = "REPO_FULL_NAME"
    }
}