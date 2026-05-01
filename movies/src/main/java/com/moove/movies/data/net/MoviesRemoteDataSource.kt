package com.moove.movies.data.net

import com.moove.movies.data.net.api.TmdbApi
import com.moove.movies.data.net.dto.MovieDetailsDTO
import com.moove.movies.data.net.dto.PopularMoviesResponseDTO
import com.moove.movies.domain.exceptions.MovieNotFoundException
import com.moove.movies.domain.exceptions.MoviesApiException
import retrofit2.HttpException

internal class MoviesRemoteDataSource(
    private val tmdbApi: TmdbApi,
) {

    suspend fun getPopular(page: Int): PopularMoviesResponseDTO = wrapApiErrors(movieId = null) {
        tmdbApi.getPopular(page = page)
    }

    suspend fun getDetails(movieId: Long): MovieDetailsDTO = wrapApiErrors(movieId = movieId) {
        tmdbApi.getMovieDetails(movieId = movieId)
    }

    private suspend inline fun <T> wrapApiErrors(movieId: Long?, block: () -> T): T =
        try {
            block()
        } catch (http: HttpException) {
            val code = http.code()
            if (code == HTTP_NOT_FOUND && movieId != null) {
                throw MovieNotFoundException(movieId = movieId, cause = http)
            } else {
                throw MoviesApiException(
                    statusCode = code,
                    message = http.message(),
                    cause = http,
                )
            }
        }

    private companion object {
        const val HTTP_NOT_FOUND = 404
    }
}
