package com.moove.tickets.analytics.event

import com.moove.tickets.domain.exceptions.TicketPurchaseException
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.parameters

/**
 * A refused purchase, in the dashboard's vocabulary. The event is a plain value; the mapping from
 * the domain failure is [toPurchaseFailed] below, so the ViewModel never chooses a reason and a
 * new case in [TicketPurchaseException] is a compile error until it has decided how it reports.
 */
data class PurchaseFailed(
    val reason: Reason,
    val requested: Int? = null,
    val max: Int? = null,
) : Event {

    enum class Reason(val wireName: String) {
        FARE_UNAVAILABLE("fare_unavailable"),
        TICKET_LIMIT_EXCEEDED("ticket_limit_exceeded"),
    }

    override val name = "purchase_failed"
    override val parameters = parameters {
        put("reason", reason.wireName)
        requested?.let { put("requested", it) }
        max?.let { put("max", it) }
    }
}

fun TicketPurchaseException.toPurchaseFailed(): PurchaseFailed = when (this) {
    is TicketPurchaseException.FareUnavailable -> PurchaseFailed(reason = PurchaseFailed.Reason.FARE_UNAVAILABLE)
    is TicketPurchaseException.TicketLimitExceeded -> PurchaseFailed(
        reason = PurchaseFailed.Reason.TICKET_LIMIT_EXCEEDED,
        requested = requested,
        max = max,
    )
}
