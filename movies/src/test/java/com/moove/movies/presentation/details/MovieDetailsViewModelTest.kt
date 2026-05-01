package com.moove.movies.presentation.details

import com.moove.core.exception.ExceptionHandler
import com.moove.movies.data.net.dto.asDomain
import com.moove.movies.data.net.dto.randomMovieDetailsDTO
import com.moove.movies.domain.use_cases.GetMovieDetailsUseCase
import com.moove.movies.presentation.details.model.asPresentation
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private val exceptionHandler = ExceptionHandler { }
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase = mockk()

    @Test
    fun `given successful fetch on create then state becomes Success with details`() = runTest {
        val domain = randomMovieDetailsDTO(id = 42L).asDomain()
        coEvery { getMovieDetailsUseCase(42L) } returns domain
        val subject = buildViewModel(movieId = 42L)

        subject.test(this) {
            runOnCreate()
            expectInitialState()
            expectState { copy(status = ScreenContentStatus.Loading) }
            expectState {
                copy(
                    status = ScreenContentStatus.Success,
                    details = domain.asPresentation(),
                )
            }
        }
    }

    @Test
    fun `given onBack when invoked then GoBack effect is emitted`() = runTest {
        coEvery { getMovieDetailsUseCase(42L) } returns randomMovieDetailsDTO(id = 42L).asDomain()
        val subject = buildViewModel(movieId = 42L)

        subject.test(this) {
            expectInitialState()
            containerHost.onBack()
            expectSideEffect(MovieDetailsEffect.GoBack)
        }
    }

    private fun buildViewModel(movieId: Long): MovieDetailsViewModel = MovieDetailsViewModel(
        exceptionHandler = exceptionHandler,
        movieId = movieId,
        getMovieDetailsUseCase = getMovieDetailsUseCase,
    )
}
