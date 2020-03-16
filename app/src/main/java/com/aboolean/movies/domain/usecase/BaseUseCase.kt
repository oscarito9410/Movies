package com.aboolean.movies.domain.usecase

interface BaseUseCase {
    fun updateFavoriteState(id: Long, isFavorite: Boolean)
}