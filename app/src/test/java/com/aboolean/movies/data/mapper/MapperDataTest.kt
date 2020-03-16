package com.aboolean.movies.data.mapper

import com.aboolean.movies.data.model.MovieData
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperDataTest {

    private val movieDataModel = MovieData(
            id = 1, title = "toy story 2",
            voteCount = 300, voteAverage = 4.2, isFavorite = false, posterPath = "poster.jpg",
            releaseDate = "19/03/2002", overview = "Amazing movie", page = 1
    )

    @Test
    fun `test verify movie data model maps correctly to movie domain model`() {
        val movieDomainModel = movieDataModel.toDomainModel()
        with(movieDomainModel) {
            assertEquals(movieDataModel.title, title)
            assertEquals(movieDataModel.voteCount, voteCount)
            assertEquals(movieDataModel.isFavorite, isFavorite)
            assertEquals(movieDataModel.posterPath, posterPath)
            assertEquals(movieDataModel.releaseDate, releaseDate)
            assertEquals(movieDataModel.overview, overview)
            assertEquals(movieDataModel.page, page)
        }
    }
}