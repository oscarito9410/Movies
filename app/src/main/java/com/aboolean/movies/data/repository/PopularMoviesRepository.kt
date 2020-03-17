@file:Suppress("UNCHECKED_CAST")

package com.aboolean.movies.data.repository

import com.aboolean.movies.data.local.MoviesDao
import com.aboolean.movies.data.model.MovieData
import com.aboolean.movies.data.model.PageMovie
import com.aboolean.movies.data.remote.MovieEndpoints
import com.aboolean.movies.domain.model.Movie
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface PopularMoviesRepository : BaseRepository {
    fun getPopular(page: Int): Maybe<List<Movie>>
}

class PopularMoviesRepositoryImpl(private val movieEndpoints: MovieEndpoints,
                                  private val moviesDao: MoviesDao) : PopularMoviesRepository {

    /**
     * Method to get the list of popular movies. Firstly we will check if the movie's page is available
     * in the local data source in case it doesn't exits then we will make a request to the api to get
     * the movies.
     * @return a list of movies sorted
     */
    override fun getPopular(page: Int): Maybe<List<Movie>> {
        return Single.concat(moviesDao.getPopularLocalMovies(page), movieEndpoints.getPopularMovies(page))
                .filter { items: Any -> mapMovies(items)?.isNotEmpty() ?: false }
                .firstElement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    mapMovies(it)
                }
    }

    private fun mapMovies(items: Any): List<Movie>? {
        //Sort movies when comes form local data source
        return if (items is Collection<*>) {
            sortMovies(items as List<MovieData>)
        } else {
            //Sort movies when comes from remote data source
            with(items as PageMovie) {
                saveOnDataBase(this)
                sortMovies(results)
            }
        }
    }

    /**
     * Method to save movies into the local data source (database)
     */
    private fun saveOnDataBase(pageMovie: PageMovie) {
        pageMovie.results?.toList().also { listMovies ->
            listMovies?.forEach {
                it.page = pageMovie.page
                moviesDao.insert(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
            }
        }
    }
}

