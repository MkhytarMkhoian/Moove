package com.moove.tickets.domain.use_cases

import com.moove.shared.faker
import com.moove.tickets.domain.TicketsRepository
import com.moove.tickets.domain.model.Fare
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BuyTicketUseCaseTest {

    private val ticketsRepository: TicketsRepository = mockk(relaxed = true)

    private val buyTicketUseCase = BuyTicketUseCase(ticketsRepository)

    @Test
    fun `On invoke should call correct method on repository`() = runTest {
        val ryderId = faker.idNumber().valid()
        val fare: Fare = mockk()
        val totalCount: Int = faker.number().randomDigitNotZero()

        buyTicketUseCase(ryderId, fare, totalCount)

        coVerify { ticketsRepository.buyTicket(ryderId, fare, totalCount) }
    }
}