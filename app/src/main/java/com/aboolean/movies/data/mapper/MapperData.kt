package com.aboolean.movies.data.mapper

import com.aboolean.movies.data.model.MovieData
import com.aboolean.movies.domain.model.Movie

fun MovieData.toDomainModel() = Movie(
        id = id, title = title ?: "", overview = overview ?: "",
        posterPath = posterPath ?: "", releaseDate = releaseDate ?: "",
        voteAverage = voteAverage ?: 0.0, voteCount = voteCount ?: 0L,
        isFavorite = isFavorite ?: false, page = page ?: 0
)