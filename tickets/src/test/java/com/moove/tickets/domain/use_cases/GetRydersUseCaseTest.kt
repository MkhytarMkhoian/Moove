package com.moove.tickets.domain.use_cases

import com.moove.tickets.domain.TicketsRepository
import com.moove.tickets.domain.model.Ryder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetRydersUseCaseTest {

    private val ticketsRepository: TicketsRepository = mockk(relaxed = true)

    private val getRydersUseCase = GetRydersUseCase(ticketsRepository)

    @Test
    fun `On invoke should return correct result`() = runTest {
        val ryders: List<Ryder> = listOf(
            mockk(),
            mockk(),
        )

        coEvery { ticketsRepository.getRyders() } returns ryders

        val result = getRydersUseCase()

        assertEquals(ryders, result)

        coVerify { ticketsRepository.getRyders() }
    }
}