package com.aboolean.movies.base

import androidx.lifecycle.ViewModel
import com.aboolean.movies.ui.model.FavoriteMoviesViewState
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    //Container which can hold multiple disposable
    protected val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    /**
     *  Method shared in both ViewModel to notify when a favorite is added or removed
     */
    protected fun getFavoriteViewStateWhenUpdate(isFavorite: Boolean): FavoriteMoviesViewState {
        return when {
            isFavorite -> FavoriteMoviesViewState.OnSuccessAddFavorite
            else -> FavoriteMoviesViewState.OnSuccessRemoveFavorite
        }
    }

    //On cleared dispose disposables to avoid memory leaks
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}