package com.aboolean.movies.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aboolean.movies.base.BaseViewModel
import com.aboolean.movies.domain.model.Movie
import com.aboolean.movies.domain.usecase.GetPopularMoviesUseCase
import com.aboolean.movies.ui.model.FavoriteMoviesViewState
import com.aboolean.movies.ui.model.PopularMoviesViewState
import com.aboolean.movies.utils.Constants
import io.reactivex.rxkotlin.addTo

class PopularMoviesViewModel(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : BaseViewModel() {

    //Private fields
    private var _popularViewState = MutableLiveData<PopularMoviesViewState>()
    private var _popularMovies = MutableLiveData<List<Movie>>()
    private var _favoriteViewState = MutableLiveData<FavoriteMoviesViewState>()

    //Public fields
    var currentPage: Int = 1

    val popularMovies: LiveData<List<Movie>>
        get() = _popularMovies

    val popularViewState: LiveData<PopularMoviesViewState>
        get() = _popularViewState

    val favoriteMoviesViewState: LiveData<FavoriteMoviesViewState>
        get() = _favoriteViewState

    //Functions
    fun fetchPopularMovies() {
        when {
            canLoadMoreMovies(currentPage) -> sendPopularMoviesRequest()
            else -> _popularViewState.postValue(PopularMoviesViewState.OnMaxPagesReached)
        }
    }

    /**
     * Update favorite movies status.
     */
    fun updateFavorite(id: Long, isFavorite: Boolean) {
        getPopularMoviesUseCase.updateFavoriteState(id, isFavorite)
        _favoriteViewState.postValue(getFavoriteViewStateWhenUpdate(isFavorite))
    }

    /**
     * Method to get popular movies.
     */
    private fun sendPopularMoviesRequest() {
        getPopularMoviesUseCase.getPopular(currentPage)
                .doOnSubscribe {
                    _popularViewState.postValue(PopularMoviesViewState.OnLoading)
                }
                .doOnSuccess {
                    //Update the current page only when is successful the request
                    currentPage++
                    _popularViewState.postValue(PopularMoviesViewState.OnSuccessFetch(currentPage))
                }
                .subscribe({
                    _popularMovies.postValue(it)
                }, {
                    _popularViewState.postValue(PopularMoviesViewState.OnError)
                }).addTo(compositeDisposable)
    }

    /**
     * Method to verify if the user can request more pages.
     */
    private fun canLoadMoreMovies(currentPage: Int) = currentPage < Constants.MAX_PAGES
}