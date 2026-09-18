package com.moove.tickets.data.local

import com.moove.tickets.domain.exceptions.TicketPurchaseException
import com.squareup.moshi.Moshi
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class TicketsLocalDataSourceTest {

    private val dataSource = TicketsLocalDataSource(Moshi.Builder().build())

    @Test
    fun `given a sold fare within the limit when buying then a receipt with a fresh transaction id is issued`() {
        val first = dataSource.buyTicket(ryderId = "Adult", fareDescription = "1 Day Pass", totalCount = 10)
        val second = dataSource.buyTicket(ryderId = "Adult", fareDescription = "1 Day Pass", totalCount = 10)

        assertTrue(first.transactionId.isNotBlank())
        assertNotEquals(first.transactionId, second.transactionId)
    }

    @Test
    fun `given a fare the ryder does not sell when buying then FareUnavailable is thrown`() {
        val e = assertFailsWith<TicketPurchaseException.FareUnavailable> {
            dataSource.buyTicket(ryderId = "Adult", fareDescription = "Weekend Pass", totalCount = 1)
        }

        assertEquals("Adult", e.ryderId)
        assertEquals("Weekend Pass", e.fare)
    }

    @Test
    fun `given an unknown ryder when buying then FareUnavailable is thrown`() {
        assertFailsWith<TicketPurchaseException.FareUnavailable> {
            dataSource.buyTicket(ryderId = "Student", fareDescription = "1 Day Pass", totalCount = 1)
        }
    }

    @Test
    fun `given more tickets than the limit when buying then TicketLimitExceeded is thrown`() {
        val e = assertFailsWith<TicketPurchaseException.TicketLimitExceeded> {
            dataSource.buyTicket(ryderId = "Adult", fareDescription = "1 Day Pass", totalCount = 11)
        }

        assertEquals(11, e.requested)
        assertEquals(10, e.max)
    }
}
