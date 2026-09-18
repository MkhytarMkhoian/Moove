package com.moove.tickets.presentation.confirmation

import androidx.lifecycle.ViewModel
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.shared.presentation.viewmodel.executeUseCase
import com.moove.tickets.analytics.event.TicketCountChanged
import com.moove.tickets.analytics.event.TicketPurchased
import com.moove.tickets.analytics.event.toPurchaseFailed
import com.moove.tickets.analytics.property.SeatPreference
import com.moove.tickets.analytics.property.TicketsPurchased
import com.moove.tickets.domain.exceptions.TicketPurchaseException
import com.moove.tickets.domain.use_cases.BuyTicketUseCase
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.fare.model.asDomain
import io.github.mkhytarmkhoian.herald.EventTrackerService
import io.github.mkhytarmkhoian.herald.PropertyTrackerService
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class ConfirmationViewModel(
    private val exceptionHandler: ExceptionHandler,
    ryderId: String,
    fare: FareModel,
    private val buyTicketUseCase: BuyTicketUseCase,
    private val analyticsEventService: EventTrackerService,
    private val analyticsPropertyService: PropertyTrackerService,
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
        analyticsEventService.track(TicketCountChanged(ticketCount))
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
        analyticsEventService.track(TicketCountChanged(ticketCount))
    }

    fun onConfirmClick() = intent {
        reduce { state.copy(status = ScreenContentStatus.Loading) }
        executeUseCase(exceptionHandler) {
            buyTicketUseCase(
                ryderId = state.ryderId,
                fare = state.fare.asDomain(),
                totalCount = state.ticketCount
            )
        }
            .onSuccess { receipt ->
                reduce { state.copy(status = ScreenContentStatus.Success) }
                analyticsEventService.track(
                    TicketPurchased(
                        TicketPurchased.Params(
                            ryderId = state.ryderId,
                            fare = state.fare.description,
                            count = state.ticketCount,
                            revenue = state.totalPrice.toDouble(),
                            currency = CURRENCY,
                            deduplicationId = receipt.transactionId,
                        )
                    )
                )
                analyticsPropertyService.set(TicketsPurchased(state.ticketCount))
                analyticsPropertyService.set(SeatPreference("window")) // unmapped for Adjust on purpose
                postSideEffect(ConfirmationEffect.ShowSuccessMessage)
            }
            .onFailure { failure ->
                reduce { state.copy(status = ScreenContentStatus.Failure) }
                // Anything else is not a purchase outcome; the exception handler already has it.
                if (failure is TicketPurchaseException) analyticsEventService.track(failure.toPurchaseFailed())
                postSideEffect(ConfirmationEffect.ShowGenericError)
            }
    }
}

private const val CURRENCY = "USD"