package com.moove.tickets.domain.use_cases

import com.moove.tickets.domain.TicketsRepository
import com.moove.tickets.domain.model.Fare
import com.moove.tickets.domain.model.TicketReceipt

class BuyTicketUseCase(
    private val ticketsRepository: TicketsRepository,
) {
    suspend operator fun invoke(ryderId: String, fare: Fare, totalCount: Int): TicketReceipt =
        ticketsRepository.buyTicket(ryderId = ryderId, fare = fare, totalCount = totalCount)
}
