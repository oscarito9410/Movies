package com.aboolean.movies.ui.popular

import android.content.res.Configuration
import androidx.lifecycle.Observer
import com.aboolean.movies.R
import com.aboolean.movies.base.BaseFragment
import com.aboolean.movies.ui.model.FavoriteMoviesViewState
import com.aboolean.movies.ui.model.PopularMoviesViewState
import com.aboolean.movies.utils.extensions.gone
import com.aboolean.movies.utils.extensions.snack
import com.aboolean.movies.utils.extensions.visible
import com.aboolean.movies.utils.recyclerview.InfiniteScrollProvider
import com.aboolean.movies.utils.recyclerview.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_home_movies.*
import org.koin.android.viewmodel.ext.android.viewModel

class PopularMoviesFragment : BaseFragment() {

    private val viewModel: PopularMoviesViewModel by viewModel()

    override fun getLayoutView(): Int = R.layout.fragment_home_movies

    override fun initView() {
        attachObservers()
        initAdapterManager()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        rvPopularMovies?.apply {
            moviesLayoutManager.spanCount = getColumnsByOrientation(isLandscape)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun attachObservers() {
        viewModel.apply {
            fetchPopularMovies()
            popularMovies.observe(this@PopularMoviesFragment, Observer {
                moviesAdapter.addNewMovies(it)
            })
            popularViewState.observe(this@PopularMoviesFragment, Observer {
                handlePopularViewState(it)
            })

            favoriteMoviesViewState.observe(this@PopularMoviesFragment, Observer {
                handleFavoriteViewState(it)
            })
        }
    }

    private fun handleFavoriteViewState(viewState: FavoriteMoviesViewState) {
        when (viewState) {
            is FavoriteMoviesViewState.OnSuccessAddFavorite -> snack(R.string.message_success_add_favorites)
            else -> snack(R.string.message_success_remove_favorites)
        }
    }

    private fun handlePopularViewState(viewState: PopularMoviesViewState) {
        when (viewState) {
            is PopularMoviesViewState.OnSuccessFetch -> loadingAnimation.gone()
            is PopularMoviesViewState.OnLoading -> loadingAnimation.visible()
            is PopularMoviesViewState.OnMaxPagesReached -> snack(R.string.message_max_pages_reached)
            is PopularMoviesViewState.OnError -> snack(R.string.message_error_fetching)
        }
    }

    private fun initAdapterManager() {
        rvPopularMovies?.apply {
            layoutManager = moviesLayoutManager
            addItemDecoration(SpacesItemDecoration(SPACE_ITEM_DECORATION))
            adapter = moviesAdapter
            addScrollListener()
            handleItemClickListener()
        }
    }

    private fun handleItemClickListener() {
        moviesAdapter.apply {
            setItemClickListener { movie ->
                movie.run {
                    val newValue = !isFavorite
                    isFavorite = newValue
                    notifyDataSetChanged()
                    viewModel.updateFavorite(id, newValue)
                }
            }
        }
    }

    private fun addScrollListener() {
        InfiniteScrollProvider().attach(rvPopularMovies, object : InfiniteScrollProvider.OnLoadMoreListener {
            override fun onLoadMore() {
                viewModel.fetchPopularMovies()
            }
        })
    }
}