package com.aboolean.movies.ui.model

import com.aboolean.movies.domain.model.Movie

/**
 * This sealed class will help us to notify to the view about changes
 * in the popular movies screen
 */
sealed class PopularMoviesViewState {
    data class OnSuccessFetch(val newPage: Int) : PopularMoviesViewState()
    object OnError : PopularMoviesViewState()
    object OnLoading : PopularMoviesViewState()
    object OnMaxPagesReached : PopularMoviesViewState()
}
/**
 * This sealed class will help us to notify to the view about changes
 * in the favorite movies screen
 */
sealed class FavoriteMoviesViewState {
    object OnSuccessRemoveFavorite : FavoriteMoviesViewState()
    object OnSuccessAddFavorite : FavoriteMoviesViewState()
    object OnEmptyFavorites : FavoriteMoviesViewState()
    data class OnResultFavorites(val movies: List<Movie>) : FavoriteMoviesViewState()
}