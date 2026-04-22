package com.moove.movies.domain

import androidx.paging.PagingData
import com.moove.movies.domain.model.MovieDetails
import com.moove.movies.domain.model.MovieSummary
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getPopular(): Flow<PagingData<MovieSummary>>

    suspend fun getDetails(movieId: Long): MovieDetails
}
