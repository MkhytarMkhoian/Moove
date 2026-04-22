package com.moove.movies.data.net

import com.moove.movies.data.net.api.TmdbApi
import com.moove.movies.data.net.dto.randomMovieDetailsDTO
import com.moove.movies.domain.exceptions.MovieNotFoundException
import com.moove.movies.domain.exceptions.MoviesApiException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class MoviesRemoteDataSourceTest {

    private val api: TmdbApi = mockk()
    private val dataSource = MoviesRemoteDataSource(api)

    @Test
    fun `given getDetails returns a dto when calling then dto is returned`() = runTest {
        val dto = randomMovieDetailsDTO(id = 7L)
        coEvery { api.getMovieDetails(movieId = 7L) } returns dto

        val result = dataSource.getDetails(7L)

        assertEquals(dto, result)
    }

    @Test
    fun `given 404 when getting details then MovieNotFoundException is thrown`() = runTest {
        coEvery { api.getMovieDetails(movieId = 99L) } throws httpException(code = 404)

        try {
            dataSource.getDetails(99L)
            fail("Expected MovieNotFoundException")
        } catch (e: MovieNotFoundException) {
            assertEquals(99L, e.movieId)
        }
    }

    @Test
    fun `given 500 when getting details then MoviesApiException is thrown`() = runTest {
        coEvery { api.getMovieDetails(movieId = 1L) } throws httpException(code = 500)

        try {
            dataSource.getDetails(1L)
            fail("Expected MoviesApiException")
        } catch (e: MoviesApiException) {
            assertEquals(500, e.statusCode)
        }
    }

    @Test
    fun `given 401 when getting popular then MoviesApiException is thrown`() = runTest {
        coEvery { api.getPopular(page = 1) } throws httpException(code = 401)

        try {
            dataSource.getPopular(page = 1)
            fail("Expected MoviesApiException")
        } catch (e: MoviesApiException) {
            assertEquals(401, e.statusCode)
        }
    }

    @Test
    fun `given 404 on popular when calling then MoviesApiException is thrown not MovieNotFoundException`() =
        runTest {
            coEvery { api.getPopular(page = 1) } throws httpException(code = 404)

            try {
                dataSource.getPopular(page = 1)
                fail("Expected MoviesApiException")
            } catch (e: MoviesApiException) {
                assertEquals(404, e.statusCode)
            } catch (e: MovieNotFoundException) {
                fail("Should not map popular 404 to MovieNotFoundException")
            }
        }

    @Test
    fun `wrapped http exceptions preserve the original cause`() = runTest {
        val cause = httpException(code = 500)
        coEvery { api.getMovieDetails(movieId = 1L) } throws cause

        try {
            dataSource.getDetails(1L)
            fail()
        } catch (e: MoviesApiException) {
            assertTrue(e.cause === cause)
        }
    }

    private fun httpException(code: Int): HttpException {
        val body = "error".toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(code, body))
    }
}
