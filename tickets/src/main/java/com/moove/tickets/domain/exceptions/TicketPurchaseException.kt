package com.moove.tickets.domain.exceptions

sealed class TicketPurchaseException(message: String) : Exception(message) {

    class FareUnavailable(val ryderId: String, val fare: String) :
        TicketPurchaseException("Fare '$fare' is not sold for ryder '$ryderId'")

    class TicketLimitExceeded(val requested: Int, val max: Int) :
        TicketPurchaseException("Requested $requested tickets, at most $max per purchase")
}
