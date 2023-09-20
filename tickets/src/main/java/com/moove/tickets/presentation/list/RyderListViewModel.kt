package com.moove.tickets.presentation.list

import androidx.lifecycle.ViewModel
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.shared.presentation.viewmodel.executeUseCase
import com.moove.tickets.domain.use_cases.GetRydersUseCase
import com.moove.tickets.presentation.list.model.RyderModel
import com.moove.tickets.presentation.list.model.asPresentation
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class RyderListViewModel(
    private val exceptionHandler: ExceptionHandler,
    private val getRydersUseCase: GetRydersUseCase,
) : ViewModel(), ContainerHost<RyderListState, RyderListEffect> {

    override val container: Container<RyderListState, RyderListEffect> = container(
        initialState = RyderListState(),
        buildSettings = {
            this.exceptionHandler =
                this@RyderListViewModel.exceptionHandler.asCoroutineExceptionHandler()
        },
    ) {
        fetchRyders()
    }

    fun onRyderClick(ryder: RyderModel) = intent {
        postSideEffect(RyderListEffect.GoToFares(ryder.id))
    }

    private fun fetchRyders() = intent {
        reduce { state.copy(status = ScreenContentStatus.Loading) }
        executeUseCase { getRydersUseCase() }
            .onSuccess {
                reduce {
                    state.copy(
                        status = ScreenContentStatus.Success,
                        ryders = it.asPresentation()
                    )
                }
            }
            .onFailure {
                reduce { state.copy(status = ScreenContentStatus.Failure) }
                postSideEffect(RyderListEffect.ShowGenericError)
            }
    }
}
