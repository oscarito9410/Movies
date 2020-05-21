package com.aboolean.movies.ui.popular

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aboolean.movies.domain.model.Movie
import com.aboolean.movies.domain.usecase.GetPopularMoviesUseCase
import com.aboolean.movies.ui.model.FavoriteMoviesViewState
import com.aboolean.movies.ui.model.PopularMoviesViewState
import com.aboolean.movies.utils.Constants
import com.aboolean.movies.utils.extensions.getOrAwaitValue
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class PopularMoviesViewModelTest {

    @JvmField
    @Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var useCase: GetPopularMoviesUseCase
    private lateinit var viewModel: PopularMoviesViewModel
    private val movieId = 1L
    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = PopularMoviesViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `fetch popular movies when max page is not reached yet and response is successful`() =
        testCoroutineScope.runBlockingTest {
            whenever(useCase.suspendGetPopular(anyInt())).thenReturn(movies)
            viewModel.run {
                currentPage = 1
                fetchPopularMovies()
                Assert.assertEquals(popularMovies.getOrAwaitValue(), movies)
                Assert.assertEquals(
                    popularViewState.getOrAwaitValue(),
                    PopularMoviesViewState.OnSuccessFetch(2)
                )
            }
        }

    @Test
    fun `fetch popular movies when max page is not reached yet and response is unsuccessful`() =
        testCoroutineScope.runBlockingTest {
            val error = Error("Network error")
            whenever(useCase.suspendGetPopular(anyInt())).thenThrow(error)
            viewModel.run {
                currentPage = 1
                fetchPopularMovies()
                Assert.assertNotEquals(
                    popularViewState.getOrAwaitValue(),
                    PopularMoviesViewState.OnSuccessFetch(2)
                )
                Assert.assertEquals(
                    popularViewState.getOrAwaitValue(),
                    PopularMoviesViewState.OnError
                )
            }
        }

    @Test
    fun `fetch popular movies when max page is reached and onMaxPagesReached is called`() =
        testCoroutineScope.runBlockingTest {
            whenever(useCase.suspendGetPopular(anyInt())).thenReturn(movies)
            viewModel.run {
                currentPage = Constants.MAX_PAGES
                fetchPopularMovies()
                Assert.assertEquals(
                    popularViewState.getOrAwaitValue(),
                    PopularMoviesViewState.OnMaxPagesReached
                )
            }
        }

    @Test
    fun `update movie successful when is favorite`() = testCoroutineScope.runBlockingTest {
        viewModel.run {
            updateFavorite(movieId, isFavorite = true)
            Assert.assertNotEquals(
                favoriteMoviesViewState.getOrAwaitValue(),
                FavoriteMoviesViewState.OnSuccessRemoveFavorite
            )
            Assert.assertEquals(
                favoriteMoviesViewState.getOrAwaitValue(),
                FavoriteMoviesViewState.OnSuccessAddFavorite
            )
        }
    }

    @Test
    fun `update movie successful when is not favorite`() = testCoroutineScope.runBlockingTest {
        viewModel.run {
            updateFavorite(movieId, isFavorite = false)
            Assert.assertEquals(
                favoriteMoviesViewState.getOrAwaitValue(),
                FavoriteMoviesViewState.OnSuccessRemoveFavorite
            )
            Assert.assertNotEquals(
                favoriteMoviesViewState.getOrAwaitValue(),
                FavoriteMoviesViewState.OnSuccessAddFavorite
            )
        }
    }

    private val movies = listOf(
        Movie(
            id = 1, title = "toy story 2",
            voteCount = 300, voteAverage = 4.2, isFavorite = false, posterPath = "poster.jpg",
            releaseDate = "19/03/2002", overview = "Amazing movie", page = 1
        )
    )
}