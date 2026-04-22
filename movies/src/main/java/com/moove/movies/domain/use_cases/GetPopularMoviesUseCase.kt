package com.moove.movies.domain.use_cases

import androidx.paging.PagingData
import com.moove.movies.domain.MoviesRepository
import com.moove.movies.domain.model.MovieSummary
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPopularMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {
    operator fun invoke(): Flow<PagingData<MovieSummary>> = moviesRepository.getPopular()
}
