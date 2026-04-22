package com.moove.movies.presentation.list

import androidx.paging.PagingData
import com.moove.core.exception.ExceptionHandler
import com.moove.movies.domain.use_cases.GetPopularMoviesUseCase
import com.moove.movies.presentation.list.model.MovieSummaryModel
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

    @Test
    fun `given a click when onMovieClick then GoToDetails effect is emitted`() = runTest {
        every { getPopularMoviesUseCase() } returns flowOf(PagingData.empty())
        val subject = MovieListViewModel(exceptionHandler, getPopularMoviesUseCase)
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
    }
}
