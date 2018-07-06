/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.tabacowang.githubpopular.util

/**
 * Extension functions for View and subclasses of View.
 */

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.BindingAdapter
import android.support.design.widget.Snackbar
import android.view.View
import me.tabacowang.githubpopular.ScrollChildSwipeRefreshLayout
import me.tabacowang.githubpopular.SingleLiveEvent
import me.tabacowang.githubpopular.popular.PopularViewModel
import me.tabacowang.githubpopular.popular.TrendViewModel


/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).show()
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(lifecycleOwner: LifecycleOwner,
                       snackbarMessageLiveEvent: SingleLiveEvent<Int>, timeLength: Int) {
    snackbarMessageLiveEvent.observe(lifecycleOwner, Observer {
        it?.let { showSnackbar(context.getString(it), timeLength) }
    })
}

/**
 * Reloads the data when the pull-to-refresh is triggered.
 *
 * Creates the `android:onRefresh` for a [SwipeRefreshLayout].
 */
@BindingAdapter("android:onRefresh")
fun ScrollChildSwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
        viewModel: ViewModel) {
    setOnRefreshListener {
        when(viewModel) {
            is PopularViewModel -> {
                viewModel.loadRepos(true)
            }
            is TrendViewModel -> {
                viewModel.start(true)
            }
        }
    }
}