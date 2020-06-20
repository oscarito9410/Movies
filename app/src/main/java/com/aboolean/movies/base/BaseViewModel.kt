package com.aboolean.movies.base

import androidx.lifecycle.ViewModel
import com.aboolean.movies.ui.model.FavoriteMoviesViewState
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {

    //Container which can hold multiple disposable
    protected val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    private val job = SupervisorJob()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
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
        job.cancelChildren()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + coroutineExceptionHandler
}