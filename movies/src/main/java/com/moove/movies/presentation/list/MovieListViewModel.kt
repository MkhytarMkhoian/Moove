package com.moove.movies.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.movies.domain.use_cases.GetPopularMoviesUseCase
import com.moove.movies.presentation.list.model.MovieSummaryModel
import com.moove.movies.presentation.list.model.asPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class MovieListViewModel @Inject constructor(
    exceptionHandler: ExceptionHandler,
    getPopularMoviesUseCase: GetPopularMoviesUseCase,
) : ViewModel(), ContainerHost<MovieListState, MovieListEffect> {

    override val container: Container<MovieListState, MovieListEffect> = container(
        initialState = MovieListState(),
        buildSettings = {
            this.exceptionHandler = exceptionHandler.asCoroutineExceptionHandler()
        },
    )

    val movies: Flow<PagingData<MovieSummaryModel>> =
        getPopularMoviesUseCase()
            .map { pagingData -> pagingData.map { it.asPresentation() } }
            .cachedIn(viewModelScope)

    fun onMovieClick(movie: MovieSummaryModel) = intent {
        postSideEffect(MovieListEffect.GoToDetails(movie.id))
    }
}
