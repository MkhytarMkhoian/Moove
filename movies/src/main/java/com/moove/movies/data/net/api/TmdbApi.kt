package com.moove.movies.data.net.api

import com.moove.movies.data.net.dto.MovieDetailsDTO
import com.moove.movies.data.net.dto.PopularMoviesResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
    ): PopularMoviesResponseDTO

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String = "en-US",
    ): MovieDetailsDTO
}
