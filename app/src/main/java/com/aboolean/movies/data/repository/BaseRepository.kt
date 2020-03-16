package com.aboolean.movies.data.repository

import com.aboolean.movies.data.mapper.toDomainModel
import com.aboolean.movies.data.model.MovieData
import com.aboolean.movies.domain.model.Movie

interface BaseRepository {
    fun sortMovies(movies: List<MovieData>?): List<Movie>? {
        return movies?.map { result ->
            result.toDomainModel()
        }?.sortedByDescending { item -> item.voteAverage }
    }
}