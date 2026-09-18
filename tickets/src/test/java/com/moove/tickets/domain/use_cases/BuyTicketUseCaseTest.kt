package com.moove.tickets.domain.use_cases

import com.moove.shared.faker
import com.moove.tickets.domain.TicketsRepository
import com.moove.tickets.domain.model.Fare
import com.moove.tickets.domain.model.TicketReceipt
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class BuyTicketUseCaseTest {

    private val ticketsRepository: TicketsRepository = mockk(relaxed = true)

    private val buyTicketUseCase = BuyTicketUseCase(ticketsRepository)

    @Test
    fun `On invoke should call correct method on repository`() = runTest {
        val ryderId = faker.idNumber().valid()
        val fare: Fare = mockk()
        val totalCount: Int = faker.number().randomDigitNotZero()

        val receipt = TicketReceipt(transactionId = "txn-1")
        coEvery { ticketsRepository.buyTicket(ryderId, fare, totalCount) } returns receipt

        val result = buyTicketUseCase(ryderId, fare, totalCount)

        assertEquals(receipt, result)
        coVerify { ticketsRepository.buyTicket(ryderId, fare, totalCount) }
    }
}