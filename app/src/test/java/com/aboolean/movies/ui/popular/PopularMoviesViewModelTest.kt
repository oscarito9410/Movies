package com.aboolean.movies.ui.popular

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aboolean.movies.domain.model.Movie
import com.aboolean.movies.domain.usecase.GetPopularMoviesUseCase
import com.aboolean.movies.ui.model.FavoriteMoviesViewState
import com.aboolean.movies.ui.model.PopularMoviesViewState
import com.aboolean.movies.utils.Constants
import com.aboolean.movies.utils.extensions.getOrAwaitValue
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Maybe
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class PopularMoviesViewModelTest {

    @JvmField
    @Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var useCase: GetPopularMoviesUseCase
    private lateinit var viewModel: PopularMoviesViewModel
    private val movieId = 1L

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = PopularMoviesViewModel(useCase)
    }

    @Test
    fun `fetch popular movies when max page is not reached yet and response is successful`() {
        whenever(useCase.getPopular(anyInt())).thenReturn(Maybe.just(movies))
        viewModel.run {
            currentPage = 1
            fetchPopularMovies()
            Assert.assertEquals(popularMovies.getOrAwaitValue(), movies)
            Assert.assertEquals(popularViewState.getOrAwaitValue(), PopularMoviesViewState.OnSuccessFetch(2))
        }
    }

    @Test
    fun `fetch popular movies when max page is not reached yet and response is unsuccessful`() {
        whenever(useCase.getPopular(anyInt())).thenReturn(Maybe.error(Exception("Network error")))
        viewModel.run {
            currentPage = 1
            fetchPopularMovies()
            Assert.assertNotEquals(popularViewState.getOrAwaitValue(), PopularMoviesViewState.OnSuccessFetch(2))
            Assert.assertEquals(popularViewState.getOrAwaitValue(), PopularMoviesViewState.OnError)
        }
    }

    @Test
    fun `fetch popular movies when max page reached and onMaxPagesReached is called`() {
        whenever(useCase.getPopular(anyInt())).thenReturn(Maybe.just(movies))
        viewModel.run {
            currentPage = Constants.MAX_PAGES
            fetchPopularMovies()
            Assert.assertEquals(popularViewState.getOrAwaitValue(), PopularMoviesViewState.OnMaxPagesReached)
        }
    }

    @Test
    fun `update movie successful when is favorite`() {
        viewModel.run {
            updateFavorite(movieId, isFavorite = true)
            Assert.assertNotEquals(favoriteMoviesViewState.getOrAwaitValue(), FavoriteMoviesViewState.OnSuccessRemoveFavorite)
            Assert.assertEquals(favoriteMoviesViewState.getOrAwaitValue(), FavoriteMoviesViewState.OnSuccessAddFavorite)
        }
    }

    @Test
    fun `update movie successful when is not favorite`() {
        viewModel.run {
            updateFavorite(movieId, isFavorite = false)
            Assert.assertEquals(favoriteMoviesViewState.getOrAwaitValue(), FavoriteMoviesViewState.OnSuccessRemoveFavorite)
            Assert.assertNotEquals(favoriteMoviesViewState.getOrAwaitValue(), FavoriteMoviesViewState.OnSuccessAddFavorite)
        }
    }

    private val movies = listOf(Movie(
            id = 1, title = "toy story 2",
            voteCount = 300, voteAverage = 4.2, isFavorite = false, posterPath = "poster.jpg",
            releaseDate = "19/03/2002", overview = "Amazing movie", page = 1
    ))
}