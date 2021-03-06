package com.aboolean.movies.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.aboolean.movies.data.local.MoviesDao
import com.aboolean.movies.data.model.MovieData
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FavoriteMoviesRepositoryTest {
    @JvmField
    @Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var movieDao: MoviesDao
    private lateinit var repositoryFavorites: FavoriteMoviesRepository
    private val movieId = 1L

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repositoryFavorites = FavoriteMoviesRepositoryImpl(movieDao)
    }

    @Test
    fun `get favorites are sorted successfully`() {
        val favoritesLiveData = MutableLiveData<List<MovieData>>()
        favoritesLiveData.postValue(movies)
        whenever(movieDao.getFavoritesMovies()).thenReturn(favoritesLiveData)
        repositoryFavorites.getFavorites().observeForever { favorites ->
            val movieDomain = favorites.first()
            val movieData = movies[1]
            Assert.assertNotNull(favorites)
            Assert.assertEquals(movieDomain.title, movieData.title)
            Assert.assertEquals(movieDomain.voteAverage, movieData.voteAverage)
            Assert.assertEquals(movieDomain.voteCount, movieData.voteCount)
        }
    }

    @Test
    fun `update movie to favorite successfully`() {
        whenever(movieDao.updateFavorite(ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyBoolean())).thenReturn(Completable.complete())
        movieDao.updateFavorite(movieId, isFavorite = true)
                .test().assertNoErrors().assertComplete().hasSubscription()
    }

    private val movies = listOf(MovieData(id = 1, title = "toy story 2",
            voteCount = 300, voteAverage = 4.2, isFavorite = false, posterPath = "poster.jpg",
            releaseDate = "19/03/2002", overview = "Amazing movie", page = 1),
            MovieData(id = 2, title = "Guason",
                    voteCount = 300, voteAverage = 9.2, isFavorite = false, posterPath = "poster.jpg",
                    releaseDate = "19/03/2002", overview = "Amazing movie", page = 1))
}