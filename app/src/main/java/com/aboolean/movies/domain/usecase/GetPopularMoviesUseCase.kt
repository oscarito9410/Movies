package com.aboolean.movies.domain.usecase

import com.aboolean.movies.data.repository.FavoriteMoviesRepository
import com.aboolean.movies.data.repository.PopularMoviesRepository
import com.aboolean.movies.domain.model.Movie
import io.reactivex.Maybe

interface GetPopularMoviesUseCase : BaseUseCase {
    fun getPopular(page: Int): Maybe<List<Movie>>
}

class GetPopularMoviesUseCaseImpl(private val repositoryPopularMovies: PopularMoviesRepository,
                                  private val repositoryFavoriteMovies: FavoriteMoviesRepository) : GetPopularMoviesUseCase {

    override fun getPopular(page: Int): Maybe<List<Movie>> {
        return repositoryPopularMovies.getPopular(page)
    }

    override fun updateFavoriteState(id: Long, isFavorite: Boolean) {
        repositoryFavoriteMovies.updateFavoriteState(id, isFavorite)
    }
}
