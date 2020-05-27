package com.aboolean.movies.data.mocks

import com.aboolean.movies.data.model.MovieData

object MockData {
    val movies = listOf(MovieData(
            id = 1, title = "toy story 2",
            voteCount = 300, voteAverage = 4.2, isFavorite = false, posterPath = "poster.jpg",
            releaseDate = "19/03/2002", overview = "Amazing movie", page = 1
        ), MovieData(
            id = 2, title = "Guason",
            voteCount = 300, voteAverage = 9.2, isFavorite = false, posterPath = "poster.jpg",
            releaseDate = "19/03/2002", overview = "Amazing movie", page = 1
        )
    )
}