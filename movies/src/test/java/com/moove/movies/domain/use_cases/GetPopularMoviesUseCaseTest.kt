package com.moove.movies.domain.use_cases

import androidx.paging.PagingData
import com.moove.movies.domain.MoviesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

class GetPopularMoviesUseCaseTest {

    private val repository: MoviesRepository = mockk()
    private val useCase = GetPopularMoviesUseCase(repository)

    @Test
    fun `given invoke when calling then delegates to repository getPopular`() {
        every { repository.getPopular() } returns flowOf(PagingData.empty())

        useCase()

        verify { repository.getPopular() }
    }
}
