package com.aboolean.movies.domain.usecase

import androidx.lifecycle.LiveData
import com.aboolean.movies.data.repository.FavoriteMoviesRepository
import com.aboolean.movies.domain.model.Movie

interface GetFavoritesMoviesUseCase : BaseUseCase {
    fun getFavorites(): LiveData<List<Movie>>
    suspend fun suspendUpdateFavoriteState(id: Long, isFavorite: Boolean) {
        TODO("You must implement if you want to update favorites using coroutines")
    }
}

class GetFavoritesMoviesUseCaseImpl(private val favoriteMoviesRepository: FavoriteMoviesRepository) : GetFavoritesMoviesUseCase {

    override fun getFavorites(): LiveData<List<Movie>> {
        return favoriteMoviesRepository.getFavorites()
    }

    override fun updateFavoriteState(id: Long, isFavorite: Boolean) {
        favoriteMoviesRepository.updateFavoriteState(id, isFavorite)
    }

    override suspend fun suspendUpdateFavoriteState(id: Long, isFavorite: Boolean) {
        favoriteMoviesRepository.suspendUpdateFavoriteState(id, isFavorite)
    }
}