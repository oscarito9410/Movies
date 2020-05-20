package com.aboolean.movies.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aboolean.movies.base.BaseViewModel
import com.aboolean.movies.domain.model.Movie
import com.aboolean.movies.domain.usecase.GetFavoritesMoviesUseCase
import com.aboolean.movies.ui.model.FavoriteMoviesViewState
import kotlinx.coroutines.launch

class FavoritesMoviesViewModel(private val getFavoritesMoviesUseCase: GetFavoritesMoviesUseCase) : BaseViewModel() {

    //Private fields
    private var _favoriteViewState = MutableLiveData<FavoriteMoviesViewState>()

    //Public fields
    val favoriteMoviesViewState: LiveData<FavoriteMoviesViewState>
        get() = _favoriteViewState

    /**
     * Live data of favorites movies in this case the fragment will be attached to this live data variable
     * to observe database changes and update the list of favorite movies in the UI.
     */
    val favoriteMovies by lazy {
        getFavoritesMoviesUseCase.getFavorites()
    }

    /**
     * Update favorite movies status
     */
    fun updateFavorite(id: Long, isFavorite: Boolean) {
        suspendUpdateAndPostFavorite(id, isFavorite)
    }

    private fun updateAndPostFavorite(id: Long, isFavorite: Boolean) {
        getFavoritesMoviesUseCase.updateFavoriteState(id, isFavorite)
        _favoriteViewState.postValue(getFavoriteViewStateWhenUpdate(isFavorite))
    }

    private fun suspendUpdateAndPostFavorite(id: Long, isFavorite: Boolean) {
        launch {
            val updateFavoriteResult =
                runCatching { getFavoritesMoviesUseCase.suspendUpdateFavoriteState(id, isFavorite) }
            updateFavoriteResult.onSuccess {
                _favoriteViewState.postValue(getFavoriteViewStateWhenUpdate(isFavorite))
            }
        }
    }

    /**
     * Handle list of movies and notify to the UI.
     */
    fun handleFavoritesListResult(movies: List<Movie>?) {
        if (movies.isNullOrEmpty()) {
            _favoriteViewState.postValue(FavoriteMoviesViewState.OnEmptyFavorites)

        } else {
            _favoriteViewState.postValue(FavoriteMoviesViewState.OnResultFavorites(movies))
        }
    }
}