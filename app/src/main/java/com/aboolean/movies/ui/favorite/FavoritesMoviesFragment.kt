package com.aboolean.movies.ui.favorite

import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.Observer
import com.aboolean.movies.R
import com.aboolean.movies.base.BaseFragment
import com.aboolean.movies.domain.model.Movie
import com.aboolean.movies.ui.model.FavoriteMoviesViewState
import com.aboolean.movies.utils.extensions.gone
import com.aboolean.movies.utils.extensions.snack
import com.aboolean.movies.utils.extensions.visible
import com.aboolean.movies.utils.recyclerview.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_home_favorites.*
import org.koin.android.viewmodel.ext.android.viewModel

class FavoritesMoviesFragment : BaseFragment() {

    private val viewModel: FavoritesMoviesViewModel by viewModel()

    override fun getLayoutView(): Int = R.layout.fragment_home_favorites

    override fun initView() {
        initAdapterManager()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        attachObservers()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        rvFavoriteMovies?.apply {
            moviesLayoutManager.spanCount = getColumnsByOrientation(isLandscape)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun attachObservers() {
        viewModel.apply {
            favoriteMovies.observe(viewLifecycleOwner, Observer {
                handleFavoritesListResult(it)

            })
            favoriteMoviesViewState.observe(viewLifecycleOwner, Observer {
                handleFavoriteViewState(it)
            })
        }
    }


    private fun handleFavoriteViewState(viewState: FavoriteMoviesViewState) {
        when (viewState) {
            is FavoriteMoviesViewState.OnSuccessAddFavorite -> snack(R.string.message_success_add_favorites)
            is FavoriteMoviesViewState.OnSuccessRemoveFavorite -> snack(R.string.message_success_remove_favorites)
            is FavoriteMoviesViewState.OnResultFavorites -> showFavoritesMovies(viewState.movies)
            is FavoriteMoviesViewState.OnEmptyFavorites -> showEmptyFavorites()
        }
    }

    private fun showEmptyFavorites() {
        rvFavoriteMovies.gone()
        tvEmptyFavoritesMovies.visible()
    }

    private fun showFavoritesMovies(movies: List<Movie>) {
        moviesAdapter.updateMovies(movies)
        rvFavoriteMovies.visible()
        tvEmptyFavoritesMovies.gone()
    }

    private fun initAdapterManager() {
        rvFavoriteMovies?.apply {
            layoutManager = moviesLayoutManager
            addItemDecoration(SpacesItemDecoration(SPACE_ITEM_DECORATION))
            adapter = moviesAdapter
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
}