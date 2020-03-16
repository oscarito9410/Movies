package com.aboolean.movies.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aboolean.movies.base.BaseViewModel
import com.aboolean.movies.domain.model.Movie
import com.aboolean.movies.domain.usecase.GetFavoritesMoviesUseCase
import com.aboolean.movies.ui.model.FavoriteMoviesViewState

class FavoritesMoviesViewModel(private val getFavoritesMoviesUseCase: GetFavoritesMoviesUseCase) : BaseViewModel() {

    //Private fields
    private var _favoriteViewState = MutableLiveData<FavoriteMoviesViewState>()

    //Public fields
    val favoriteMoviesViewState: LiveData<FavoriteMoviesViewState>
        get() = _favoriteViewState

    val favoriteMovies by lazy {
        getFavoritesMoviesUseCase.getFavorites()
    }

    fun updateFavorite(id: Long, isFavorite: Boolean) {
        getFavoritesMoviesUseCase.updateFavoriteState(id, isFavorite)
        _favoriteViewState.postValue(getFavoriteViewStateWhenUpdate(isFavorite))
    }

    fun handleFavoritesListResult(movies: List<Movie>?) {
        if (movies.isNullOrEmpty()) {
            _favoriteViewState.postValue(FavoriteMoviesViewState.OnEmptyFavorites)

        } else {
            _favoriteViewState.postValue(FavoriteMoviesViewState.onResultFavorites(movies))
        }
    }
}