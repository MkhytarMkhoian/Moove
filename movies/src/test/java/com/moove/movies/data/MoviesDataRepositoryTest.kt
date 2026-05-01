package com.moove.movies.data

import com.moove.movies.data.net.MoviesRemoteDataSource
import com.moove.movies.data.net.dto.randomMovieDetailsDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MoviesDataRepositoryTest {

    private val remote: MoviesRemoteDataSource = mockk()
    private val repository = MoviesDataRepository(
        remoteDataSource = remote,
        backgroundDispatcher = Dispatchers.Unconfined,
    )

    @Test
    fun `given remote returns a dto when fetching details then maps to domain`() = runTest {
        val dto = randomMovieDetailsDTO(id = 5L)
        coEvery { remote.getDetails(5L) } returns dto

        val result = repository.getDetails(5L)

        assertEquals(5L, result.id)
        assertEquals(dto.title, result.title)
    }
}
