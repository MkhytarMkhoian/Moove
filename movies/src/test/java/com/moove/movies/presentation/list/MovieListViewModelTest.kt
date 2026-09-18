package com.moove.movies.presentation.list

import androidx.paging.PagingData
import com.moove.core.exception.ExceptionHandler
import com.moove.movies.analytics.event.MovieListRetried
import com.moove.movies.domain.use_cases.GetPopularMoviesUseCase
import com.moove.movies.presentation.list.model.MovieSummaryModel
import io.github.mkhytarmkhoian.herald.testing.FakeAnalyticsProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    private val exceptionHandler = ExceptionHandler { }
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase = mockk()
    private val analytics = FakeAnalyticsProvider()

    @Test
    fun `given a click when onMovieClick then GoToDetails effect is emitted`() = runTest {
        every { getPopularMoviesUseCase() } returns flowOf(PagingData.empty())
        val subject = MovieListViewModel(exceptionHandler, getPopularMoviesUseCase, analytics)
        val clicked = MovieSummaryModel(
            id = 42L,
            title = "The Matrix",
            posterPath = null,
            rating = 8.2f,
            releaseYear = "1999",
        )
        subject.test(this) {
            expectInitialState()
            containerHost.onMovieClick(clicked)
            expectSideEffect(MovieListEffect.GoToDetails(42L))
        }

        analytics.assertTracked("movie_opened") {
            param("movie_id", 42L)
            param("title", "The Matrix")
        }
    }

    @Test
    fun `given the ViewModel is created then the screen view is tracked exactly once`() = runTest {
        every { getPopularMoviesUseCase() } returns flowOf(PagingData.empty())
        val subject = MovieListViewModel(exceptionHandler, getPopularMoviesUseCase, analytics)

        subject.test(this) {
            runOnCreate()
            expectInitialState()
        }

        analytics.assertTracked("movie_list_shown") { noParameters() }
        analytics.assertNothingElseTracked()
    }

    @Test
    fun `given a pull to refresh when onRefresh then the refresh is tracked and nothing else is`() = runTest {
        every { getPopularMoviesUseCase() } returns flowOf(PagingData.empty())
        val subject = MovieListViewModel(exceptionHandler, getPopularMoviesUseCase, analytics)

        subject.test(this) {
            expectInitialState()
            containerHost.onRefresh()
        }

        analytics.assertTracked("movie_list_refreshed") { noParameters() }
        analytics.assertNothingElseTracked()
    }

    @Test
    fun `given a retry after a failed page when onRetry then the stage is tracked by its wire name`() = runTest {
        every { getPopularMoviesUseCase() } returns flowOf(PagingData.empty())
        val subject = MovieListViewModel(exceptionHandler, getPopularMoviesUseCase, analytics)

        subject.test(this) {
            expectInitialState()
            containerHost.onRetry(MovieListRetried.Stage.NEXT_PAGE)
        }

        analytics.assertTracked("movie_list_retried") { param("stage", "next_page") }
        analytics.assertNothingElseTracked()
    }
}
