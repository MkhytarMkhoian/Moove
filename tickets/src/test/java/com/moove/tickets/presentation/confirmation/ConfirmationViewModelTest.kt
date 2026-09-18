package com.moove.tickets.presentation.confirmation

import com.moove.core.exception.ExceptionHandler
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.tickets.domain.model.Fare
import com.moove.tickets.domain.model.Ryder
import com.moove.tickets.domain.model.TicketReceipt
import com.moove.tickets.domain.exceptions.TicketPurchaseException
import com.moove.tickets.domain.model.randomRyder
import com.moove.tickets.analytics.event.TicketPurchased
import com.moove.tickets.domain.use_cases.BuyTicketUseCase
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.fare.model.asDomain
import com.moove.tickets.presentation.fare.model.asPresentation
import io.github.mkhytarmkhoian.herald.testing.AnalyticsRecord
import io.github.mkhytarmkhoian.herald.testing.FakeAnalyticsProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import org.orbitmvi.orbit.test.test

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
    private val analytics = FakeAnalyticsProvider()


    private fun createViewModel() = ConfirmationViewModel(
        exceptionHandler = exceptionHandler,
        buyTicketUseCase = buyTicketUseCase,
        ryderId = ryderId,
        fare = fare,
        analyticsEventService = analytics,
        analyticsPropertyService = analytics,
    )

    @Test
    fun `On Confirm click post effect`() = runTest {
        coEvery { buyTicketUseCase(ryderId = any(), fare = any(), totalCount = any()) } returns TicketReceipt("txn-1")

        createViewModel().test(this, initialState = defaultState) {
            containerHost.onConfirmClick()
            expectState { copy(status = ScreenContentStatus.Loading) }
            expectState { copy(status = ScreenContentStatus.Success) }
            expectSideEffect(ConfirmationEffect.ShowSuccessMessage)
        }

        coVerify {
            buyTicketUseCase(
                ryderId = ryderId,
                fare = fare.asDomain(),
                totalCount = defaultState.ticketCount
            )
        }
        analytics.assertTracked("ticket_purchased") {
            param("ryder_id", ryderId)
            param("fare", fare.description)
            param("count", 1)
            param("total", defaultState.totalPrice.toDouble())
        }
        val purchased = analytics.records.filterIsInstance<AnalyticsRecord.Tracked>().single { it.event is TicketPurchased }
        assertEquals("txn-1", (purchased.event as TicketPurchased).deduplicationId)
        analytics.assertNothingElseTracked()
        analytics.assertPropertySet("tickets_purchased", 1)
        analytics.assertPropertySet("seat_preference", "window")
        // A property is set before the event that should carry it? Not here — the purchase is
        // the event, the property describes the user afterwards; the timeline pins the order.
        assertEquals(
            listOf("ticket_purchased", "tickets_purchased", "seat_preference"),
            analytics.records.mapNotNull {
                when (it) {
                    is AnalyticsRecord.Tracked -> it.event.name
                    is AnalyticsRecord.PropertySet -> it.property.name
                    else -> null
                }
            },
        )
    }

    @Test
    fun `On Confirm click get error should post effect`() = runTest {
        val error = TicketPurchaseException.TicketLimitExceeded(requested = 11, max = 10)
        coEvery {
            buyTicketUseCase(
                ryderId = ryderId,
                fare = fare.asDomain(),
                totalCount = defaultState.ticketCount
            )
        } throws error

        createViewModel().test(this, initialState = defaultState) {
            containerHost.onConfirmClick()
            expectState { copy(status = ScreenContentStatus.Loading) }
            expectState { copy(status = ScreenContentStatus.Failure) }
            expectSideEffect(ConfirmationEffect.ShowGenericError)
        }

        analytics.assertTracked("purchase_failed") {
            param("reason", "ticket_limit_exceeded")
            param("requested", 11)
            param("max", 10)
        }
        analytics.assertNotTracked("ticket_purchased")

        coVerify {
            buyTicketUseCase(
                ryderId = ryderId,
                fare = fare.asDomain(),
                totalCount = defaultState.ticketCount
            )
        }
    }

    @Test
    fun `given a failure that is not a purchase refusal when confirming then no purchase_failed is tracked`() = runTest {
        coEvery { buyTicketUseCase(ryderId = any(), fare = any(), totalCount = any()) } throws RuntimeException("test")

        createViewModel().test(this, initialState = defaultState) {
            containerHost.onConfirmClick()
            expectState { copy(status = ScreenContentStatus.Loading) }
            expectState { copy(status = ScreenContentStatus.Failure) }
            expectSideEffect(ConfirmationEffect.ShowGenericError)
        }

        analytics.assertNotTracked("purchase_failed")
        analytics.assertNotTracked("ticket_purchased")
    }

    @Test
    fun `On Increment Ticket click should update state`() = runTest {
        val ticketCount = defaultState.ticketCount + 1
        val totalPrice = defaultState.fare.price * ticketCount

        createViewModel().test(this, initialState = defaultState) {
            containerHost.onIncrementTicketClick()
            expectState {
                copy(
                    ticketCount = ticketCount,
                    totalPrice = totalPrice
                )
            }
        }

        analytics.assertTracked("ticket_count_changed") { param("count", ticketCount) }
    }

    @Test
    fun `On Decrement Ticket click should update state`() = runTest {
        val ticketCount = defaultState.ticketCount - 1
        val totalPrice = defaultState.fare.price * ticketCount

        createViewModel().test(this, initialState = defaultState) {
            containerHost.onDecrementTicketClick()
            expectState {
                copy(
                    ticketCount = ticketCount,
                    totalPrice = totalPrice
                )
            }
        }
    }
}