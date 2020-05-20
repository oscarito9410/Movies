package com.aboolean.movies.domain.usecase

import com.aboolean.movies.data.repository.FavoriteMoviesRepository
import com.aboolean.movies.data.repository.PopularMoviesRepository
import com.aboolean.movies.domain.model.Movie
import io.reactivex.Maybe

interface GetPopularMoviesUseCase : BaseUseCase {
    fun getPopular(page: Int): Maybe<List<Movie>>
    suspend fun suspendGetPopular(page : Int) : List<Movie>{
        TODO("You must implement this method if you want to use coroutines")
    }
    suspend fun suspendUpdateFavoriteState(id: Long, isFavorite: Boolean) {
        TODO("You must implement if you want to update favorites using coroutines")
    }
}

class GetPopularMoviesUseCaseImpl(private val repositoryPopularMovies: PopularMoviesRepository,
                                  private val repositoryFavoriteMovies: FavoriteMoviesRepository) : GetPopularMoviesUseCase {

    override fun getPopular(page: Int): Maybe<List<Movie>> {
        return repositoryPopularMovies.getPopular(page)
    }

    override fun updateFavoriteState(id: Long, isFavorite: Boolean) {
        repositoryFavoriteMovies.updateFavoriteState(id, isFavorite)
    }

    override suspend fun suspendGetPopular(page: Int) : List<Movie> {
        return repositoryPopularMovies.suspendGetPopular(page)
    }

    override suspend fun suspendUpdateFavoriteState(id: Long, isFavorite: Boolean) {
        repositoryFavoriteMovies.suspendUpdateFavoriteState(id, isFavorite)
    }
}
