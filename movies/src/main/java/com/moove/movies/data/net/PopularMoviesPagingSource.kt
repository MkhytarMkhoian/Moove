package com.moove.movies.data.net

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moove.movies.data.net.dto.asDomain
import com.moove.movies.domain.model.MovieSummary

internal class PopularMoviesPagingSource(
    private val remoteDataSource: MoviesRemoteDataSource,
) : PagingSource<Int, MovieSummary>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieSummary> {
        val page = params.key ?: INITIAL_PAGE
        return try {
            val response = remoteDataSource.getPopular(page = page)
            LoadResult.Page(
                data = response.results.asDomain(),
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (page >= response.totalPages) null else page + 1,
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieSummary>): Int? {
        val anchor = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchor) ?: return null
        return closestPage.prevKey?.plus(1) ?: closestPage.nextKey?.minus(1)
    }

    private companion object {
        const val INITIAL_PAGE = 1
    }
}
