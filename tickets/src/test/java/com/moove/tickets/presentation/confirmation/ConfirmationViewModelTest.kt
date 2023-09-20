package com.moove.tickets.presentation.confirmation

import com.moove.core.exception.ExceptionHandler
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.tickets.domain.model.Fare
import com.moove.tickets.domain.model.Ryder
import com.moove.tickets.domain.model.randomRyder
import com.moove.tickets.domain.use_cases.BuyTicketUseCase
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.fare.model.asDomain
import com.moove.tickets.presentation.fare.model.asPresentation
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test

class ConfirmationViewModelTest {

    companion object {
        private val ryder: Ryder = randomRyder()
        private val ryderId: String = ryder.id
        private val faresList: List<Fare> = ryder.fares
        private val fare: FareModel = ryder.fares.first().asPresentation()

        private val defaultState = ConfirmationState(
            status = ScreenContentStatus.Idle,
            ryderId = ryderId,
            fare = fare,
            ticketCount = 1,
            totalPrice = 0f,
        )
    }

    private val buyTicketUseCase: BuyTicketUseCase = mockk(relaxed = true)
    private val exceptionHandler = mockk<ExceptionHandler>(relaxed = true)

    private fun TestScope.createViewModel(
        state: ConfirmationState = defaultState,
    ) = ConfirmationViewModel(
        exceptionHandler = exceptionHandler,
        buyTicketUseCase = buyTicketUseCase,
        ryderId = ryderId,
        fare = fare,
    ).test(
        initialState = state,
        buildSettings = { isolateFlow = false }
    )

    @Test
    fun `On Confirm click post effect`() = runTest {
        createViewModel(defaultState)
            .testIntent { onConfirmClick() }
            .assert(defaultState) {
                states(
                    { copy(status = ScreenContentStatus.Loading) },
                    { copy(status = ScreenContentStatus.Success) }
                )
                postedSideEffects(ConfirmationEffect.ShowSuccessMessage)
            }

        coVerify {
            buyTicketUseCase(
                ryderId = ryderId,
                fare = fare.asDomain(),
                totalCount = defaultState.ticketCount
            )
        }
    }

    @Test
    fun `On Confirm click get error should post effect`() = runTest {
        val error = RuntimeException("test")
        coEvery {
            buyTicketUseCase(
                ryderId = ryderId,
                fare = fare.asDomain(),
                totalCount = defaultState.ticketCount
            )
        } throws error

        createViewModel(defaultState)
            .testIntent { onConfirmClick() }
            .assert(defaultState) {
                states(
                    { copy(status = ScreenContentStatus.Loading) },
                    { copy(status = ScreenContentStatus.Failure) }
                )
                postedSideEffects(ConfirmationEffect.ShowGenericError)
            }

        coVerify {
            buyTicketUseCase(
                ryderId = ryderId,
                fare = fare.asDomain(),
                totalCount = defaultState.ticketCount
            )
        }
    }

    @Test
    fun `On Increment Ticket click should update state`() = runTest {
        val ticketCount = defaultState.ticketCount + 1
        val totalPrice = defaultState.fare.price * ticketCount

        createViewModel(defaultState)
            .testIntent { onIncrementTicketClick() }
            .assert(defaultState) {
                states(
                    {
                        copy(
                            ticketCount = ticketCount,
                            totalPrice = totalPrice
                        )
                    },
                )
            }
    }

    @Test
    fun `On Decrement Ticket click should update state`() = runTest {
        val ticketCount = defaultState.ticketCount - 1
        val totalPrice = defaultState.fare.price * ticketCount

        createViewModel(defaultState)
            .testIntent { onDecrementTicketClick() }
            .assert(defaultState) {
                states(
                    {
                        copy(
                            ticketCount = ticketCount,
                            totalPrice = totalPrice
                        )
                    },
                )
            }
    }
}