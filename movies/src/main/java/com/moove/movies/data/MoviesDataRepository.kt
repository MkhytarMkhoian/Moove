package com.moove.movies.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.moove.movies.data.net.MoviesRemoteDataSource
import com.moove.movies.data.net.PopularMoviesPagingSource
import com.moove.movies.data.net.dto.asDomain
import com.moove.movies.di.IoDispatcher
import com.moove.movies.domain.MoviesRepository
import com.moove.movies.domain.model.MovieDetails
import com.moove.movies.domain.model.MovieSummary
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Singleton
internal class MoviesDataRepository @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    @IoDispatcher private val backgroundDispatcher: CoroutineDispatcher,
) : MoviesRepository {

    override fun getPopular(): Flow<PagingData<MovieSummary>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PopularMoviesPagingSource(remoteDataSource) },
        ).flow

    override suspend fun getDetails(movieId: Long): MovieDetails =
        withContext(backgroundDispatcher) {
            remoteDataSource.getDetails(movieId).asDomain()
        }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
