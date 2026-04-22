package com.moove.movies.domain.use_cases

import com.moove.movies.domain.MoviesRepository
import com.moove.movies.domain.model.MovieDetails
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {
    suspend operator fun invoke(movieId: Long): MovieDetails = moviesRepository.getDetails(movieId)
}
