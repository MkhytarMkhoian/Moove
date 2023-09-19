package com.moove.tickets.domain.use_cases

import com.moove.tickets.domain.TicketsRepository
import com.moove.tickets.domain.model.Fare

class GetFaresByIdUseCase(
    private val ticketsRepository: TicketsRepository,
) {
    suspend operator fun invoke(ryderId: String): List<Fare> {
        return ticketsRepository.getFares(ryderId)
    }
}
