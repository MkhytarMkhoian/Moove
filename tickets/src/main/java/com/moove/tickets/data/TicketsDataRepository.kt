package  com.moove.tickets.data

import com.moove.tickets.data.local.TicketsLocalDataSource
import com.moove.tickets.data.local.dto.asDomain
import com.moove.tickets.domain.TicketsRepository
import com.moove.tickets.domain.model.Fare
import com.moove.tickets.domain.model.Ryder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TicketsDataRepository(
    private val ticketsLocalDataSource: TicketsLocalDataSource,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : TicketsRepository {

    override suspend fun getRyders(): List<Ryder> = withContext(backgroundDispatcher) {
        val data = ticketsLocalDataSource.getData()
        data.map { it.value.asDomain(it.key) }
    }

    override suspend fun getFares(ryderId: String): List<Fare> = withContext(backgroundDispatcher) {
        val data = ticketsLocalDataSource.getData()
        data[ryderId]?.fares?.asDomain() ?: emptyList()
    }

    override suspend fun buyTicket(ryderId: String, fare: Fare, totalCount: Int) {
        // Implement request to the server
    }
}
