package com.moove.tickets.data

import com.moove.tickets.data.dto.randomRyderDTO
import com.moove.tickets.data.local.TicketsLocalDataSource
import com.moove.tickets.data.local.dto.RyderDTO
import com.moove.tickets.data.local.dto.asDomain
import com.moove.tickets.domain.model.Fare
import com.moove.tickets.domain.model.Ryder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class TicketsDataRepositoryTest {

    private val ticketsLocalDataSource: TicketsLocalDataSource = mockk(relaxed = true)

    private val ticketsRepository = TicketsDataRepository(ticketsLocalDataSource)

    @Test
    fun `On get data should return correct result`() = runTest {
        val ryder1Id = "Ryder1"
        val ryder2Id = "Ryder2"
        val ryder3Id = "Ryder3"

        val ryder1 = randomRyderDTO()
        val ryder2 = randomRyderDTO()
        val ryder3 = randomRyderDTO()

        val ryders: List<Ryder> = listOf(
            ryder1.asDomain(ryder1Id),
            ryder2.asDomain(ryder2Id),
            ryder3.asDomain(ryder3Id),
        )

        val data: Map<String, RyderDTO> = mapOf(
            ryder1Id to ryder1,
            ryder2Id to ryder2,
            ryder3Id to ryder3,
        )

        coEvery { ticketsLocalDataSource.getData() } returns data

        val result = ticketsRepository.getRyders()

        assertEquals(ryders, result)

        coVerify { ticketsLocalDataSource.getData() }
    }

    @Test
    fun `On get fares should return correct result`() = runTest {
        val ryder1Id = "Ryder1"
        val ryder2Id = "Ryder2"
        val ryder3Id = "Ryder3"

        val ryder1 = randomRyderDTO()
        val ryder2 = randomRyderDTO()
        val ryder3 = randomRyderDTO()

        val fares: List<Fare> = ryder2.asDomain(ryder2Id).fares

        val data: Map<String, RyderDTO> = mapOf(
            ryder1Id to ryder1,
            ryder2Id to ryder2,
            ryder3Id to ryder3,
        )

        coEvery { ticketsLocalDataSource.getData() } returns data

        val result = ticketsRepository.getFares(ryder2Id)

        assertEquals(fares, result)

        coVerify { ticketsLocalDataSource.getData() }
    }
}