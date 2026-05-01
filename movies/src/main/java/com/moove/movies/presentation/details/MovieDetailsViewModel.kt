package com.moove.movies.presentation.details

import androidx.lifecycle.ViewModel
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.movies.domain.use_cases.GetMovieDetailsUseCase
import com.moove.movies.presentation.details.model.asPresentation
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.shared.presentation.viewmodel.executeUseCase
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class MovieDetailsViewModel(
    exceptionHandler: ExceptionHandler,
    private val movieId: Long,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
) : ViewModel(), ContainerHost<MovieDetailsState, MovieDetailsEffect> {

    override val container: Container<MovieDetailsState, MovieDetailsEffect> = container(
        initialState = MovieDetailsState(),
        buildSettings = {
            this.exceptionHandler = exceptionHandler.asCoroutineExceptionHandler()
        },
    ) {
        fetchDetails()
    }

    fun onRetry() = intent { fetchDetails() }

    fun onBack() = intent { postSideEffect(MovieDetailsEffect.GoBack) }

    private suspend fun SimpleSyntax<MovieDetailsState, MovieDetailsEffect>.fetchDetails() {
        reduce { state.copy(status = ScreenContentStatus.Loading) }
        executeUseCase { getMovieDetailsUseCase(movieId) }
            .onSuccess { details ->
                reduce {
                    state.copy(
                        status = ScreenContentStatus.Success,
                        details = details.asPresentation(),
                    )
                }
            }
            .onFailure {
                reduce { state.copy(status = ScreenContentStatus.Failure) }
                postSideEffect(MovieDetailsEffect.ShowGenericError)
            }
    }
}
