package com.moove.tickets.domain.use_cases

import com.moove.tickets.domain.TicketsRepository
import com.moove.tickets.domain.model.Ryder

class GetRydersUseCase(
    private val ticketsRepository: TicketsRepository,
) {
    suspend operator fun invoke(): List<Ryder> = ticketsRepository.getRyders()
}
