package com.aboolean.movies.ui.model

import com.aboolean.movies.domain.model.Movie

sealed class PopularMoviesViewState {
    data class OnSuccessFetch(val newPage: Int) : PopularMoviesViewState()
    object OnError : PopularMoviesViewState()
    object OnLoading : PopularMoviesViewState()
    object OnMaxPagesReached : PopularMoviesViewState()
}

sealed class FavoriteMoviesViewState {
    object OnSuccessRemoveFavorite : FavoriteMoviesViewState()
    object OnSuccessAddFavorite : FavoriteMoviesViewState()
    object OnEmptyFavorites : FavoriteMoviesViewState()
    data class onResultFavorites(val movies: List<Movie>) : FavoriteMoviesViewState()
}