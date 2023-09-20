package com.moove.tickets.domain.use_cases

import com.moove.shared.faker
import com.moove.tickets.domain.TicketsRepository
import com.moove.tickets.domain.model.Fare
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetFaresByIdUseCaseTest {

    private val ticketsRepository: TicketsRepository = mockk(relaxed = true)

    private val getFaresByIdUseCase = GetFaresByIdUseCase(ticketsRepository)

    @Test
    fun `On invoke should return correct result`() = runTest {
        val ryderId = faker.idNumber().valid()

        val fares: List<Fare> = listOf(
            mockk(),
            mockk(),
        )

        coEvery { ticketsRepository.getFares(ryderId) } returns fares

        val result = getFaresByIdUseCase(ryderId)

        assertEquals(fares, result)

        coVerify { ticketsRepository.getFares(ryderId) }
    }
}