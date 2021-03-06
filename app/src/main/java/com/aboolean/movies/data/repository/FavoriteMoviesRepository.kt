package com.aboolean.movies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.aboolean.movies.data.local.MoviesDao
import com.aboolean.movies.domain.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface FavoriteMoviesRepository : BaseRepository {
    fun getFavorites(): LiveData<List<Movie>>
    fun updateFavoriteState(id: Long, isFavorite: Boolean)
}

class FavoriteMoviesRepositoryImpl(private val moviesDao: MoviesDao) : FavoriteMoviesRepository {

    /**
     * @return A liveData object within list of movies. In this case liveData is helpful because it will
     * respond to updates inside database and then will notified to the MVVM.
     */
    override fun getFavorites(): LiveData<List<Movie>> {
        return Transformations.map(moviesDao.getFavoritesMovies()) {
            sortMovies(it)
        }
    }

    override fun updateFavoriteState(id: Long, isFavorite: Boolean) {
        moviesDao.updateFavorite(id, isFavorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }
}