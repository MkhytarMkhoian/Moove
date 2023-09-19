package com.moove.tickets.domain

import com.moove.tickets.domain.model.Fare
import com.moove.tickets.domain.model.Ryder

interface TicketsRepository {
    suspend fun getRyders(): List<Ryder>
    suspend fun getFares(ryderId: String): List<Fare>
    suspend fun buyTicket(ryderId: String, fare: Fare, totalCount: Int)
}
