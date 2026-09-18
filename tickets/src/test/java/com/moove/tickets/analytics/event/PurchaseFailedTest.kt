package com.moove.tickets.analytics.event

import com.moove.tickets.domain.exceptions.TicketPurchaseException
import io.github.mkhytarmkhoian.herald.AnalyticsValue
import org.junit.Test
import kotlin.test.assertEquals

class PurchaseFailedTest {

    @Test
    fun `given a fare unavailable failure when mapping then only the reason is reported`() {
        val cause = TicketPurchaseException.FareUnavailable(ryderId = "Adult", fare = "Weekend Pass")

        val event = cause.toPurchaseFailed()

        assertEquals(PurchaseFailed(reason = PurchaseFailed.Reason.FARE_UNAVAILABLE), event)
        assertEquals(mapOf("reason" to AnalyticsValue.String("fare_unavailable")), event.parameters)
    }

    @Test
    fun `given a ticket limit failure when mapping then reason and both counts are reported`() {
        val cause = TicketPurchaseException.TicketLimitExceeded(requested = 11, max = 10)

        val event = cause.toPurchaseFailed()

        assertEquals(
            PurchaseFailed(reason = PurchaseFailed.Reason.TICKET_LIMIT_EXCEEDED, requested = 11, max = 10),
            event,
        )
        assertEquals(
            mapOf(
                "reason" to AnalyticsValue.String("ticket_limit_exceeded"),
                "requested" to AnalyticsValue.Int(11),
                "max" to AnalyticsValue.Int(10),
            ),
            event.parameters,
        )
    }

    @Test
    fun `given no extras when building parameters then the optional keys are absent`() {
        val event = PurchaseFailed(reason = PurchaseFailed.Reason.TICKET_LIMIT_EXCEEDED)

        assertEquals(setOf("reason"), event.parameters.keys)
    }
}
