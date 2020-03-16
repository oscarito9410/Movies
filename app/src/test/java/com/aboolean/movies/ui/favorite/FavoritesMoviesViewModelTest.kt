package com.aboolean.movies.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aboolean.movies.domain.model.Movie
import com.aboolean.movies.domain.usecase.GetFavoritesMoviesUseCase
import com.aboolean.movies.ui.model.FavoriteMoviesViewState
import com.aboolean.movies.utils.extensions.getOrAwaitValue
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FavoritesMoviesViewModelTest {

    @JvmField
    @Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var useCase: GetFavoritesMoviesUseCase
    private lateinit var viewModel: FavoritesMoviesViewModel
    private val movieId = 1L

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = FavoritesMoviesViewModel(useCase)
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
    fun `handle favorites list when it is empty`() {
        viewModel.run {
            handleFavoritesListResult(emptyList())
            Assert.assertEquals(favoriteMoviesViewState.getOrAwaitValue(), FavoriteMoviesViewState.OnEmptyFavorites)
            Assert.assertNotEquals(favoriteMoviesViewState.getOrAwaitValue(), FavoriteMoviesViewState.onResultFavorites(emptyList()))
        }
    }


    @Test
    fun `handle favorites list when it is not empty`() {
        viewModel.run {
            handleFavoritesListResult(movies)
            Assert.assertEquals(favoriteMoviesViewState.getOrAwaitValue(), FavoriteMoviesViewState.onResultFavorites(movies))
            Assert.assertNotEquals(favoriteMoviesViewState.getOrAwaitValue(), FavoriteMoviesViewState.OnEmptyFavorites)
        }
    }

    private val movies = listOf(Movie(
            id = 1, title = "toy story 2",
            voteCount = 300, voteAverage = 4.2, isFavorite = false, posterPath = "poster.jpg",
            releaseDate = "19/03/2002", overview = "Amazing movie", page = 1
    ))
}