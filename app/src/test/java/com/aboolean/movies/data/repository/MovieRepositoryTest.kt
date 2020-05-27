package com.aboolean.movies.data.repository

import com.aboolean.movies.data.local.MoviesDao
import com.aboolean.movies.data.mocks.MockData.movies
import com.aboolean.movies.data.model.PageMovie
import com.aboolean.movies.data.remote.MovieEndpoints
import com.aboolean.movies.rules.CoroutineTestRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.operators.single.SingleJust
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MovieRepositoryTest {
    @Mock
    private lateinit var movieEndPoints: MovieEndpoints
    @Mock
    private lateinit var movieDao: MoviesDao
    private lateinit var repositoryPopularMovies: PopularMoviesRepository
    private val movieId = 1
    private val messageException = "Network Exception"
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        repositoryPopularMovies = PopularMoviesRepositoryImpl(
            movieEndPoints,
            movieDao,
            coroutinesTestRule.testDispatcherProvider
        )
    }

    @Test
    fun `get popular movies from local data source successfully`() {
        //Returning data from local data source
        whenever(movieDao.getPopularLocalMovies(movieId)).thenReturn(SingleJust.just(movies))
        //Returning empty data from network data source
        whenever(movieEndPoints.getPopularMovies(movieId)).thenReturn(Single.just(PageMovie(page = movieId, totalPage = 500, results = emptyList())))
        repositoryPopularMovies.getPopular(movieId).test().assertNoErrors().assertValue {
            val movieDomain = it.first()
            val movieData = movies[1]
            movieDomain.isFavorite == movieData.isFavorite && movieDomain.overview == movieData.overview
                    && movieDomain.page == movieData.page && movieDomain.posterPath == movieData.posterPath
                    && movieDomain.releaseDate == movieData.releaseDate &&
                    movieDomain.voteAverage == movieData.voteAverage
        }
    }

    @Test
    fun `get popular movies from local and remote data source unsuccessfully`() {
        //Both data sources will return an exception
        whenever(movieDao.getPopularLocalMovies(1)).thenReturn(Single.error(Exception(messageException)))
        whenever(movieEndPoints.getPopularMovies(1)).thenReturn(Single.error(Exception(messageException)))
        repositoryPopularMovies.getPopular(1).test().assertError {
            //Asset exception message is correct
            it.message == messageException
                    && it is Exception
        }
    }

    @Test
    fun `get popular movies from network data source successfully`() {
        //Returning an empty list from local data source and then request data to network data source
        whenever(movieDao.getPopularLocalMovies(movieId)).thenReturn(Single.just(emptyList()))
        //Request data from network data source
        whenever(movieEndPoints.getPopularMovies(movieId)).thenReturn(
            Single.just(
                PageMovie(
                    page = movieId, totalPage = 500,
                    results = movies
                )
            )
        )
        //Mock response when saving data to local data source
        whenever(movieDao.insert(any())).thenReturn(Completable.complete())
        repositoryPopularMovies.getPopular(movieId).test().assertNoErrors().assertValue {
            val movieDomain = it.first()
            val movieData = movies[1]
            movieDomain.isFavorite == movieData.isFavorite && movieDomain.overview == movieData.overview
                    && movieDomain.page == movieData.page && movieDomain.posterPath == movieData.posterPath
                    && movieDomain.releaseDate == movieData.releaseDate &&
                    movieDomain.voteAverage == movieData.voteAverage
        }
    }

    @Test
    fun `get popular movies from network data source successfully using coroutines`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            //Returning an empty list from local data source and then request data to network data source
            whenever(movieDao.suspendGetPopularLocalMovies(movieId)).thenReturn(emptyList())
            //Request data from network data source
            whenever(movieEndPoints.suspendGetPopularMovies(movieId)).thenReturn(PageMovie(page = movieId, totalPage = 500, results = movies))
            //Mock response when saving data to local data source
            whenever(movieDao.suspendInsert(any())).then { Result.success(Unit) }
            val result = runCatching { repositoryPopularMovies.suspendGetPopular(movieId) }
            val movieDomain = result.getOrDefault(emptyList()).first()
            val movieData = movies[1]

            Assert.assertEquals(result.isSuccess, true)
            Assert.assertEquals(result.isFailure, false)
            Assert.assertEquals(movieDomain.isFavorite, movieData.isFavorite)
            Assert.assertEquals(movieDomain.overview, movieData.overview)
            Assert.assertEquals(movieDomain.page, movieData.page)
            Assert.assertEquals(movieDomain.posterPath, movieData.posterPath)
            Assert.assertEquals(movieDomain.releaseDate, movieData.releaseDate)
            Assert.assertEquals(movieDomain.voteAverage, movieData.voteAverage)
        }

    @Test
    fun `get popular movies from local and remote data source unsuccessfully using coroutines`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val error = Error()
            //Both data sources will return an exception
            whenever(movieDao.suspendGetPopularLocalMovies(1)).thenThrow(error)
            whenever(movieEndPoints.suspendGetPopularMovies(1)).thenThrow(error)

            val result = runCatching { repositoryPopularMovies.suspendGetPopular(1) }
            Assert.assertEquals(result.isSuccess, false)
            Assert.assertEquals(result.isFailure, true)
        }


    @Test
    fun `verify movies are sorted successfully`() {
        //Returning data from local data source
        whenever(movieDao.getPopularLocalMovies(movieId)).thenReturn(SingleJust.just(movies))
        //Returning empty data from network data source
        whenever(movieEndPoints.getPopularMovies(movieId)).thenReturn(Single.just(PageMovie(page = movieId, totalPage = 500, results = emptyList())))
        repositoryPopularMovies.getPopular(movieId).test().assertNoErrors().assertValue {
            val movieDomain = it.first()
            movieDomain.title == "Guason" && movieDomain.voteAverage == 9.2 &&
                    !movieDomain.isFavorite && movieDomain.overview == "Amazing movie"
        }
    }
}