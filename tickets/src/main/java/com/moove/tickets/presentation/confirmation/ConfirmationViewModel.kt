package com.moove.tickets.presentation.confirmation

import androidx.lifecycle.ViewModel
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.shared.presentation.viewmodel.executeUseCase
import com.moove.tickets.domain.use_cases.BuyTicketUseCase
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.fare.model.asDomain
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class ConfirmationViewModel(
    private val exceptionHandler: ExceptionHandler,
    ryderId: String,
    fare: FareModel,
    private val buyTicketUseCase: BuyTicketUseCase
) : ViewModel(), ContainerHost<ConfirmationState, ConfirmationEffect> {

    override val container: Container<ConfirmationState, ConfirmationEffect> = container(
        initialState = ConfirmationState(
            ryderId = ryderId,
            fare = fare,
            ticketCount = 1,
            totalPrice = fare.price,
        ),
        buildSettings = {
            this.exceptionHandler =
                this@ConfirmationViewModel.exceptionHandler.asCoroutineExceptionHandler()
        },
    )

    fun onIncrementTicketClick() = intent {
        val ticketCount = state.ticketCount + 1
        reduce {
            state.copy(
                ticketCount = ticketCount,
                totalPrice = state.fare.price * ticketCount
            )
        }
    }

    fun onDecrementTicketClick() = intent {
        if (state.ticketCount == 0) return@intent
        val ticketCount = state.ticketCount - 1
        reduce {
            state.copy(
                ticketCount = ticketCount,
                totalPrice = state.fare.price * ticketCount
            )
        }
    }

    fun onConfirmClick() = intent {
        reduce { state.copy(status = ScreenContentStatus.Loading) }
        executeUseCase {
            buyTicketUseCase(
                ryderId = state.ryderId,
                fare = state.fare.asDomain(),
                totalCount = state.ticketCount
            )
        }
            .onSuccess {
                reduce { state.copy(status = ScreenContentStatus.Success) }
                postSideEffect(ConfirmationEffect.ShowSuccessMessage)
            }
            .onFailure {
                reduce { state.copy(status = ScreenContentStatus.Failure) }
                postSideEffect(ConfirmationEffect.ShowGenericError)
            }
    }
}
