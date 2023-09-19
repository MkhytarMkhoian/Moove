package com.moove.tickets.presentation.fare

import androidx.lifecycle.ViewModel
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.shared.presentation.viewmodel.executeUseCase
import com.moove.tickets.domain.use_cases.GetFaresByIdUseCase
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.fare.model.asPresentation
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class FareListViewModel(
    private val exceptionHandler: ExceptionHandler,
    private val ryderId: String,
    private val getFaresByIdUseCase: GetFaresByIdUseCase,
) : ViewModel(), ContainerHost<FareListState, FareListEffect> {

    override val container: Container<FareListState, FareListEffect> = container(
        initialState = FareListState(),
        buildSettings = {
            this.exceptionHandler =
                this@FareListViewModel.exceptionHandler.asCoroutineExceptionHandler()
        },
    ) {
        fetchFares()
    }

    fun onFareClick(fare: FareModel) = intent {
        postSideEffect(FareListEffect.GoToConfirmation(ryderId, fare))
    }

    private fun fetchFares() = intent {
        reduce { state.copy(status = ScreenContentStatus.Loading) }
        executeUseCase { getFaresByIdUseCase(ryderId) }
            .onSuccess {
                reduce {
                    state.copy(
                        status = ScreenContentStatus.Success,
                        fares = it.asPresentation()
                    )
                }
            }
            .onFailure {
                reduce { state.copy(status = ScreenContentStatus.Failure) }
                postSideEffect(FareListEffect.ShowGenericError)
            }
    }
}
