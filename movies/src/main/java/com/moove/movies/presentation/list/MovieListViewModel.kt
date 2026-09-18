package com.moove.movies.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.movies.analytics.event.MovieListRefreshed
import com.moove.movies.analytics.event.MovieListRetried
import com.moove.movies.analytics.event.MovieListScreenViewed
import com.moove.movies.analytics.event.MovieOpened
import com.moove.movies.domain.use_cases.GetPopularMoviesUseCase
import com.moove.movies.presentation.list.model.MovieSummaryModel
import com.moove.movies.presentation.list.model.asPresentation
import io.github.mkhytarmkhoian.herald.EventTrackerService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class MovieListViewModel(
    exceptionHandler: ExceptionHandler,
    getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val analyticsEventService: EventTrackerService,
) : ViewModel(), ContainerHost<MovieListState, MovieListEffect> {

    override val container: Container<MovieListState, MovieListEffect> = container(
        initialState = MovieListState(),
        buildSettings = {
            this.exceptionHandler = exceptionHandler.asCoroutineExceptionHandler()
        },
    ) {
        // Once per ViewModel, so a rotation or a return from details does not count again.
        analyticsEventService.track(MovieListScreenViewed)
    }

    val movies: Flow<PagingData<MovieSummaryModel>> =
        getPopularMoviesUseCase()
            .map { pagingData -> pagingData.map { it.asPresentation() } }
            .cachedIn(viewModelScope)

    fun onMovieClick(movie: MovieSummaryModel) = intent {
        analyticsEventService.track(MovieOpened(movieId = movie.id, title = movie.title))
        postSideEffect(MovieListEffect.GoToDetails(movie.id))
    }

    fun onRefresh() = intent { analyticsEventService.track(MovieListRefreshed) }

    fun onRetry(stage: MovieListRetried.Stage) = intent { analyticsEventService.track(MovieListRetried(stage)) }
}
