package com.moove.movies.domain.use_cases

import com.moove.movies.data.net.dto.randomMovieDetailsDTO
import com.moove.movies.data.net.dto.asDomain
import com.moove.movies.domain.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMovieDetailsUseCaseTest {

    private val repository: MoviesRepository = mockk()
    private val useCase = GetMovieDetailsUseCase(repository)

    @Test
    fun `given invoke when calling then delegates to repository with id`() = runTest {
        val domain = randomMovieDetailsDTO(id = 42L).asDomain()
        coEvery { repository.getDetails(42L) } returns domain

        val result = useCase(42L)

        assertEquals(domain, result)
        coVerify { repository.getDetails(42L) }
    }
}
