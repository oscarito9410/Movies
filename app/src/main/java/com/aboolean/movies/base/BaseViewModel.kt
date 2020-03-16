package com.aboolean.movies.base

import androidx.lifecycle.ViewModel
import com.aboolean.movies.ui.model.FavoriteMoviesViewState
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    protected fun getFavoriteViewStateWhenUpdate(isFavorite: Boolean): FavoriteMoviesViewState {
        return when {
            isFavorite -> FavoriteMoviesViewState.OnSuccessAddFavorite
            else -> FavoriteMoviesViewState.OnSuccessRemoveFavorite
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}