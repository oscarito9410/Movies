package com.aboolean.movies.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.aboolean.movies.data.model.MovieData
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MoviesDataBaseTest {
    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var moviesDao: MoviesDao
    private lateinit var movieDatabase: MoviesDataBase

    private val singleMovieNotFavorite = MovieData(id = 1, title = "Guason",
            voteCount = 300, voteAverage = 9.2, isFavorite = false, posterPath = "poster.jpg",
            releaseDate = "19/03/2002", overview = "Amazing movie", page = 1)

    private val singleMovieFavorite = MovieData(id = 2, title = "Parasito",
            voteCount = 350, voteAverage = 9.2, isFavorite = true, posterPath = "poster.jpg",
            releaseDate = "19/03/2002", overview = "Amazing movie", page = 1)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        movieDatabase = Room.inMemoryDatabaseBuilder(
                context, MoviesDataBase::class.java).allowMainThreadQueries().build()
        moviesDao = movieDatabase.moviesDao()
    }

    @Test
    @Throws(Exception::class)
    fun query_movie_by_page_is_successful() {
        moviesDao.insert(singleMovieNotFavorite).subscribe()
        val moviesResult = moviesDao.getPopularLocalMovies(page = 1).blockingGet()
        Assert.assertEquals(singleMovieNotFavorite, moviesResult.first())
    }

    @Test
    @Throws(Exception::class)
    fun query_add_favorite_movie_is_successful() {
        moviesDao.insert(singleMovieNotFavorite).subscribe()
        moviesDao.updateFavorite(id = 1, isFavorite = true).subscribe()
        val moviesResult = moviesDao.getPopularLocalMovies(page = 1).blockingGet()
        Assert.assertEquals(true, moviesResult.first().isFavorite)
    }

    @Test
    @Throws(Exception::class)
    fun query_remove_favorite_movie_is_successful() {
        moviesDao.insert(singleMovieFavorite).subscribe()
        moviesDao.updateFavorite(id = 2, isFavorite = false).subscribe()
        val moviesResult = moviesDao.getPopularLocalMovies(page = 1).blockingGet()
        Assert.assertEquals(false, moviesResult.first().isFavorite)
    }

    @Test
    @Throws(Exception::class)
    fun query_get_favorites_is_successful() {
        moviesDao.insert(singleMovieFavorite).subscribe()
        val moviesResult = moviesDao.getPopularLocalMovies(page = 1).blockingGet()
        Assert.assertEquals(1, moviesResult.size)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        movieDatabase.close()
    }
}